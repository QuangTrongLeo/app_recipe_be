package recipe_be.mapper;

import org.mapstruct.Mapper;
import recipe_be.dto.response.UserResponse;
import recipe_be.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponseList(List<User> users);
}
