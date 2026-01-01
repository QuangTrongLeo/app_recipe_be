package recipe_be.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import recipe_be.dto.response.ImageResponse;
import recipe_be.entity.Image;
import recipe_be.enums.ErrorCode;
import recipe_be.exception.AppException;
import recipe_be.mapper.ImageMapper;
import recipe_be.repository.ImageRepository;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    // ===== LẤY TẤT CẢ ẢNH =====
    public List<ImageResponse> getAllImages() {
        return imageMapper.toImageResponseList(imageRepository.findAll());
    }

    // ==== LẤY ẢNH BẰNG ID =====
    public ImageResponse getImageById(String id) {
        Image image = getById(id);
        return imageMapper.toImageResponse(image);
    }

    // ==== XÓA ẢNH BẰNG ID =====
    public void deleteById(String id) {
        Image image = getById(id);

        try {
            String url = image.getUrl();
            if (url != null && url.contains("/")) {
                // Cắt public_id nếu URL đúng định dạng Cloudinary
                int start = url.lastIndexOf("/") + 1;
                int end = url.lastIndexOf(".");
                String publicId;

                if (end > start) {
                    publicId = url.substring(start, end);
                    // Xóa ảnh trên Cloudinary
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                } else {
                    System.out.println("URL không có đuôi file, bỏ qua xóa Cloudinary: " + url);
                }
            }

            // Dù sao cũng xóa trong MongoDB
            imageRepository.delete(image);
        } catch (Exception e) {
            throw new RuntimeException("Xóa ảnh thất bại: " + e.getMessage());
        }
    }

    // Upload từ MultipartFile
    public Image uploadAndSave(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = uploadResult.get("secure_url").toString();

            Image image = buildImage(url);

            return imageRepository.save(image);
        } catch (Exception e) {
            throw new RuntimeException("Upload ảnh thất bại: " + e.getMessage());
        }
    }

    // Xóa ảnh khỏi Cloudinary và MongoDB theo URL
    public void deleteByUrl(String url) {
        try {
            if (url == null || url.isEmpty()) return;

            // Lấy public_id từ URL (Cloudinary)
            // URL dạng: https://res.cloudinary.com/<cloud_name>/image/upload/v123456/abc123.jpg
            String publicId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            // Xóa trong MongoDB
            imageRepository.findByUrl(url).ifPresent(imageRepository::delete);

        } catch (Exception e) {
            throw new RuntimeException("Xóa ảnh thất bại: " + e.getMessage());
        }
    }

    public Image getById(String id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }
    private Image buildImage(String url) {
        return Image.builder().url(url).build();
    }
}


