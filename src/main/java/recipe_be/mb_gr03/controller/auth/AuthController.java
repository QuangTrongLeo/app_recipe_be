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
    public ResponseEntity<ApiResp<String>> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request);
            ApiResp<String> apiResp = new ApiResp<>(HttpStatus.OK.value(), null, "Tạo tài khoản thành công!");
            return ResponseEntity.ok(apiResp);
        } catch (IllegalArgumentException ex) {
            ApiResp<String> apiResp = new ApiResp<>(HttpStatus.BAD_REQUEST.value(), null, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResp);
        } catch (Exception ex) {
            ApiResp<String> apiResp = new ApiResp<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null,
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
