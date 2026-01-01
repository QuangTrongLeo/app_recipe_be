package recipe_be.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.user.UpdateProfileRequest;
import recipe_be.dto.response.ApiResponse;
import recipe_be.dto.response.UserResponse;
import recipe_be.service.CartService;
import recipe_be.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final CartService cartService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/profile")
    public ApiResponse<UserResponse> updateProfile(@ModelAttribute UpdateProfileRequest request) {
        UserResponse updatedUser = userService.updateProfileByEmail(request);
        return ApiResponse.<UserResponse>builder()
                .result(updatedUser)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/profile")
    public ApiResponse<UserResponse> getProfile() {
        UserResponse user = userService.getUserById();
        return ApiResponse.<UserResponse>builder()
                .result(user)
                .build();
    }
}
