package recipe_be.mb_gr03.mapper.auth;

import org.springframework.stereotype.Component;
import recipe_be.mb_gr03.dto.response.auth.TokenResponse;

@Component
public class TokenMapper {

    public TokenResponse toTokenResponse(String userId, String accessToken, String refreshToken) {
        return new TokenResponse(userId, accessToken, refreshToken);
    }
}
