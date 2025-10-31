package recipe_be.mapper.auth;

import org.springframework.stereotype.Component;
import recipe_be.dto.response.auth.TokenResponse;

@Component
public class TokenMapper {

    public TokenResponse toTokenResponse(String userId, String accessToken, String refreshToken) {
        return new TokenResponse(userId, accessToken, refreshToken);
    }
}
