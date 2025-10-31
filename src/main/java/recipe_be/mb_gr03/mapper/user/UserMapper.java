package recipe_be.mb_gr03.mapper.user;

import org.springframework.stereotype.Component;
import recipe_be.mb_gr03.dto.response.user.UserResponse;
import recipe_be.mb_gr03.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        if (user == null) return null;

        return new UserResponse(
                user.getEmail(),
                user.getUsername(),
                user.getAvatar(),
                user.getBio(),
                user.getRole() != null ? user.getRole().name() : null,
                user.getCreatedAt(),
                user.getFavoriteRecipes()
        );
    }

    public List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }
}
