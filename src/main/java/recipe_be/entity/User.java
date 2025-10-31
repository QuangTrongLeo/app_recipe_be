package recipe_be.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import recipe_be.enums.Role;

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
    private Role role;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    private List<FavoriteRecipe> favoriteRecipes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FavoriteRecipe {
        private String recipeId;
    }
}
