package recipe_be.mb_gr03.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipe_be.mb_gr03.dto.request.auth.LoginRequest;
import recipe_be.mb_gr03.dto.request.auth.RegisterRequest;
import recipe_be.mb_gr03.dto.response.auth.TokenResponse;
import recipe_be.mb_gr03.entity.User;
import recipe_be.mb_gr03.mapper.auth.TokenMapper;
import recipe_be.mb_gr03.repository.user.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenMapper tokenMapper;

    // ===== ĐĂNG KÝ TÀI KHOẢN =====
    public void register(RegisterRequest request) {
        userService.createUser(request);
    }

    // ===== ĐĂNG NHẬP =====
    public TokenResponse login(LoginRequest request) {
        User user = userService.findUserByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return tokenMapper.toTokenResponse(accessToken, refreshToken);
    }
}
