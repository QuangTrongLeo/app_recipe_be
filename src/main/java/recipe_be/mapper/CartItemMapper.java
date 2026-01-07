package recipe_be.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import recipe_be.dto.response.cart.CartItemResponse;
import recipe_be.entity.CartItem;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class, RecipeMapper.class})
public interface CartItemMapper {
    
    @Mapping(target = "recipe", source = "recipe")
    CartItemResponse toResponse(CartItem cartItem);
}
