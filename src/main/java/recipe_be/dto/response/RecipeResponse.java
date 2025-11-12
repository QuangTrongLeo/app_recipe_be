package recipe_be.dto.response;

import lombok.*;
import recipe_be.entity.Category;
import recipe_be.entity.Ingredient;
import recipe_be.entity.Nutrition;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponse {
    private String id;
    private String name;
    private String description;
    private String image;
    private String instructions;
    private int time;

    private CategoryResponse category;
    private List<IngredientItemResponse> ingredients;
    private List<NutritionItemResponse> nutritions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IngredientItemResponse {
        private IngredientResponse ingredient;
        private double quantity;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NutritionItemResponse {
        private NutritionResponse nutrition;
        private double value;
    }
}

