package recipe_be.mb_gr03.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipe_be.mb_gr03.dto.request.auth.LoginRequest;
import recipe_be.mb_gr03.dto.request.auth.RegisterRequest;
import recipe_be.mb_gr03.dto.response.ApiResp;
import recipe_be.mb_gr03.dto.response.auth.TokenResponse;
import recipe_be.mb_gr03.service.auth.AuthService;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.recipe.app.url}/auth")
public class AuthController {
    private final AuthService authService;

    // ===== REGISTER =====
    @PostMapping("/register")
    public ResponseEntity<ApiResp<Boolean>> register(@RequestBody RegisterRequest request) {
        try {
            boolean success = authService.register(request);

            if (success) {
                ApiResp<Boolean> apiResp = new ApiResp<>(HttpStatus.OK.value(), true, "Tạo tài khoản thành công!");
                return ResponseEntity.ok(apiResp);
            } else {
                ApiResp<Boolean> apiResp = new ApiResp<>(HttpStatus.BAD_REQUEST.value(), false, "Tạo tài khoản thất bại!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResp);
            }

        } catch (IllegalArgumentException ex) {
            ApiResp<Boolean> apiResp = new ApiResp<>(HttpStatus.BAD_REQUEST.value(), false, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResp);
        } catch (Exception ex) {
            ApiResp<Boolean> apiResp = new ApiResp<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), false,
                    "Đăng ký thất bại: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResp);
        }
    }

    // ===== LOGIN =====
    @PostMapping("/login")
    public ResponseEntity<ApiResp<TokenResponse>> login(@RequestBody LoginRequest request) {
        try {
            TokenResponse token = authService.login(request);
            ApiResp<TokenResponse> resp = new ApiResp<>(HttpStatus.OK.value(), token, "Đăng nhập thành công!");
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            ApiResp<TokenResponse> resp = new ApiResp<>(HttpStatus.UNAUTHORIZED.value(), null,
                    "Đăng nhập thất bại: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
    }
}
