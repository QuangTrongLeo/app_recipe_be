package recipe_be.mb_gr03.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipe_be.mb_gr03.dto.request.auth.RegisterRequest;
import recipe_be.mb_gr03.entity.User;
import recipe_be.mb_gr03.enums.EnumRole;
import recipe_be.mb_gr03.repository.user.UserRepository;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ===== TẠO TÀI KHOẢN ===
    public void createUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Tài khoản này đã tồn tại!");
        }

        User user = buildUser(request);
        userRepository.save(user);
    }

    // ===== HÀM TẠO USER CHUẨN HÓA ===
    private User buildUser(RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .avatar(null)
                .bio(null)
                .role(EnumRole.USER)
                .createdAt(new Date())
                .favoriteRecipes(List.of())
                .build();
    }
}
