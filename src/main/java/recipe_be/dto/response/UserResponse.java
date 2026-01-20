package recipe_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipe_be.entity.FavoriteRecipe;
import recipe_be.entity.User;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String username;
    private String avatar;
    private String bio;
    private String role;
    private Date createdAt;
    private List<FavoriteRecipe> favoriteRecipes;
}
