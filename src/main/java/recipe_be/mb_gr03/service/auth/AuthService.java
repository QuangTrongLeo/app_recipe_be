package recipe_be.mb_gr03.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipe_be.mb_gr03.dto.request.auth.RegisterRequest;
import recipe_be.mb_gr03.repository.user.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    // ===== ĐĂNG KÝ TÀI KHOẢN
    public void register(RegisterRequest request) {
        userService.createUser(request);
    }
}
