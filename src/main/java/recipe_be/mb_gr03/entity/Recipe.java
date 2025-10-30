package recipe_be.mb_gr03.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import recipe_be.mb_gr03.enums.EnumCategory;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "recipes")
public class Recipe {
    @Id
    private String id;
    private EnumCategory category;
    private String name;
    private String description;
    private String image;
    private String instructions;
    private int time;

    private List<IngredientItem> ingredients;
    private List<NutritionItem> nutritions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IngredientItem {
        private String ingredientId;
        private int quantity;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NutritionItem {
        private String nutritionId;
        private double value;
    }
}
