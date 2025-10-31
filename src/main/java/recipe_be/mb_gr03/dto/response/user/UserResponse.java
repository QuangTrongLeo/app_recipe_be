package recipe_be.mb_gr03.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipe_be.mb_gr03.entity.User;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String username;
    private String avatar;
    private String bio;
    private String role;
    private Date createdAt;
    private List<User.FavoriteRecipe> favoriteRecipes;
}
