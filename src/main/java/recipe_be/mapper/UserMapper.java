package recipe_be.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import recipe_be.dto.response.UserResponse;
import recipe_be.entity.User;
import recipe_be.enums.Role;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", expression = "java(user.getRole() != null ? user.getRole().name() : null)")
    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> users);
}
