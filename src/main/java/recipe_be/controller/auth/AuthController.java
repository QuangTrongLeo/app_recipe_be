package recipe_be.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipe_be.dto.request.auth.LoginRequest;
import recipe_be.dto.request.auth.RegisterRequest;
import recipe_be.dto.response.APIResponse;
import recipe_be.dto.response.auth.TokenResponse;
import recipe_be.service.auth.AuthService;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.recipe.app.url}/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public APIResponse register(@RequestBody RegisterRequest request) {
        boolean success = authService.register(request);
        return APIResponse.builder(success).build();
    }

    @PostMapping("/login")
    public APIResponse login(@RequestBody LoginRequest request) {
        TokenResponse token = authService.login(request);
        return APIResponse.builder(token).build();
    }
}
