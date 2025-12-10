package recipe_be.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientItem {
    private String ingredientId;
    private double quantity;
}


