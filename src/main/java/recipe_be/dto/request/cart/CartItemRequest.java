package recipe_be.dto.request.cart;

import lombok.Data;
import recipe_be.entity.IngredientItem;

import java.util.List;

@Data
public class CartItemRequest {
    String recipeId;
    Integer quantity;
    List<IngredientItem> ingredients;

}
