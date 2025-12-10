package recipe_be.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import recipe_be.dto.response.UserResponse;
import recipe_be.entity.User;
import recipe_be.enums.Role;

import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponseList(List<User> users);
    default Date map(Long value) {
        if (value == null) return null;
        return new Date(value);
    }
}
