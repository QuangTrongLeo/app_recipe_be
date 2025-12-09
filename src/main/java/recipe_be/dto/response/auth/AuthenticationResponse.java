package recipe_be.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipe_be.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    String token;
    User user;
}
