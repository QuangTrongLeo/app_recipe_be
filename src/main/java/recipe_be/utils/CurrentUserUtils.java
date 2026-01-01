package recipe_be.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import recipe_be.enums.ErrorCode;
import recipe_be.exception.AppException;

public class CurrentUserUtils {

    private CurrentUserUtils() {}
    public static String getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        // JWT authentication object
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getClaimAsString("userId");
        }
        throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
    }
}
