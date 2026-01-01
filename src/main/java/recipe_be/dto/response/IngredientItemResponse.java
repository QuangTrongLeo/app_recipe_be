package recipe_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientItemResponse {
    private IngredientResponse ingredient;
    private double quantity;
}