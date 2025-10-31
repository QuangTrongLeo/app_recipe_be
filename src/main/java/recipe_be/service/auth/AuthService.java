package recipe_be.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipe_be.dto.request.auth.LoginRequest;
import recipe_be.dto.request.auth.RegisterRequest;
import recipe_be.dto.response.auth.TokenResponse;
import recipe_be.entity.User;
import recipe_be.mapper.auth.TokenMapper;
import recipe_be.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenMapper tokenMapper;

    // ===== ĐĂNG KÝ TÀI KHOẢN =====
    public boolean register(RegisterRequest request) {
        userService.createUser(request);
        return true;
    }

    // ===== ĐĂNG NHẬP =====
    public TokenResponse login(LoginRequest request) {
        User user = userService.getUserByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return tokenMapper.toTokenResponse(user.getId(), accessToken, refreshToken);
    }
}
