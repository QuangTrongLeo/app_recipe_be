package recipe_be.mb_gr03.dto.request.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileRequest {
    private String username;
    private String bio;
    private MultipartFile avatar;
}
