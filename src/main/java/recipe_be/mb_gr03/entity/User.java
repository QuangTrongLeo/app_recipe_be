package recipe_be.mb_gr03.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import recipe_be.mb_gr03.enums.EnumRole;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String username;
    private String avatar;
    private String bio;
    private EnumRole role;
    private Date createdAt;

    private List<FavoriteRecipe> favoriteRecipes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FavoriteRecipe {
        private String recipeId;
    }
}
