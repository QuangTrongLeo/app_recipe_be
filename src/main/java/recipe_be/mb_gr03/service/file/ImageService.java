package recipe_be.mb_gr03.service.file;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import recipe_be.mb_gr03.entity.Image;
import recipe_be.mb_gr03.repository.file.ImageRepository;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;

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

    private Image buildImage(String url) {
        return Image.builder().url(url).build();
    }
}


