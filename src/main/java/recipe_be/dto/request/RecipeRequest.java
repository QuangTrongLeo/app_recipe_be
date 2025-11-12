package recipe_be.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import recipe_be.entity.Recipe;

import java.util.List;

@Data
public class RecipeRequest {
    private String categoryId;
    private String name;
    private String description;
    private String instructions;
    private int time;
    private MultipartFile image;
    private List<Recipe.IngredientItem> ingredients;
    private List<Recipe.NutritionItem> nutritions;
}
