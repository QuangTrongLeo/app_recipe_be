package recipe_be.mb_gr03.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipe_be.mb_gr03.dto.request.user.UpdateProfileRequest;
import recipe_be.mb_gr03.dto.response.ApiResp;
import recipe_be.mb_gr03.entity.User;
import recipe_be.mb_gr03.service.auth.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.recipe.app.url}/users")
public class UserController {
    private final UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<ApiResp<User>> updateProfile(@RequestBody UpdateProfileRequest request) {
        try {
            User updatedUser = userService.updateProfileByEmail(request);
            ApiResp<User> resp = new ApiResp<>(HttpStatus.OK.value(), updatedUser, "Cập nhật profile thành công!");
            return ResponseEntity.ok(resp);
        } catch (SecurityException e) {
            // Trường hợp user không được phép (403)
            ApiResp<User> resp = new ApiResp<>(HttpStatus.FORBIDDEN.value(), null, "Bạn không có quyền cập nhật profile này!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
        } catch (Exception e) {
            // Lỗi khác (500)
            ApiResp<User> resp = new ApiResp<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "Lỗi server: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }
}
