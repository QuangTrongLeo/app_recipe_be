package recipe_be.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.user.UpdateProfileRequest;
import recipe_be.dto.response.APIResponse;
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
    public APIResponse updateProfile(@ModelAttribute UpdateProfileRequest request) {
        UserResponse updatedUser = userService.updateProfileByEmail(request);
        return APIResponse.builder(updatedUser).build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/profile")
    public APIResponse getProfile() {
        UserResponse user = userService.getUserById();
        return APIResponse.builder(user).build();
    }
}
