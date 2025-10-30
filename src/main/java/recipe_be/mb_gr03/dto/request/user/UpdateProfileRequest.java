package recipe_be.mb_gr03.dto.request.user;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String username;
    private String avatar;
    private String bio;
}
