package recipe_be.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import recipe_be.dto.response.ImageResponse;
import recipe_be.entity.Image;
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
            // Lấy public_id từ URL (Cloudinary)
            // URL ví dụ: https://res.cloudinary.com/demo/image/upload/v1731234567/abc123.jpg
            String publicId = image.getUrl().substring(
                    image.getUrl().lastIndexOf("/") + 1,
                    image.getUrl().lastIndexOf(".")
            );

            // Xóa ảnh khỏi Cloudinary
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            // Xóa bản ghi trong MongoDB
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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ảnh với ID: " + id));
    }
    private Image buildImage(String url) {
        return Image.builder().url(url).build();
    }
}


