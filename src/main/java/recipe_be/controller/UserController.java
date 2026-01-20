package recipe_be.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.user.UpdateProfileRequest;
import recipe_be.dto.response.ApiResponse;
import recipe_be.dto.response.UserResponse;
import recipe_be.service.CartService;
import recipe_be.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/profile")
    public ApiResponse<UserResponse> updateProfile(@ModelAttribute UpdateProfileRequest request) {
        UserResponse updatedUser = userService.updateProfileByUserId(request);
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/total")
    public ApiResponse<Long> totalUsers() {
        return ApiResponse.<Long>builder()
                .result(userService.totalUsers())
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteUser(@PathVariable String id) {
        return ApiResponse.<Boolean>builder()
                .result(userService.deleteUser(id))
                .build();
    }
}
