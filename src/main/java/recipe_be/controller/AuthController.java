package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipe_be.dto.request.auth.LoginRequest;
import recipe_be.dto.request.auth.RegisterRequest;
import recipe_be.dto.response.ApiResponse;
import recipe_be.dto.response.auth.AuthenticationResponse;
import recipe_be.service.auth.AuthService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<Boolean> register(@RequestBody RegisterRequest request) {
        boolean success = authService.register(request);
        return ApiResponse.<Boolean>builder()
                .result(success)
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        AuthenticationResponse token = authService.login(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(token)
                .build();
    }
}
