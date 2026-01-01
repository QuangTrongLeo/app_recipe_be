package recipe_be.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipe_be.dto.response.IngredientItemResponse;
import recipe_be.dto.response.RecipeResponse;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private RecipeResponse recipe;
    private List<IngredientItemResponse> items;
}
