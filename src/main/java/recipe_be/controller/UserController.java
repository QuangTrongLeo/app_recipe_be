package recipe_be.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.user.UpdateProfileRequest;
import recipe_be.dto.response.APIResponse;
import recipe_be.dto.response.UserResponse;
import recipe_be.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.recipe.app.url}/users")
public class UserController {
    private final UserService userService;

    @PutMapping("/profile")
    public APIResponse updateProfile(@ModelAttribute UpdateProfileRequest request) {
        UserResponse updatedUser = userService.updateProfileByEmail(request);
        return APIResponse.builder(updatedUser).build();
    }
}
