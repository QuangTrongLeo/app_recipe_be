package recipe_be.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import recipe_be.entity.IngredientItem;
import recipe_be.entity.NutritionItem;
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
    private List<IngredientItem> ingredients;
    private List<NutritionItem> nutritions;
}
