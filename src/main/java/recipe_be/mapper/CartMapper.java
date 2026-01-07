package recipe_be.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import recipe_be.dto.response.cart.CartResponse;
import recipe_be.entity.Cart;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {
    @Mapping(target = "items", source = "items")
    CartResponse toResponse(Cart cart);
    
}
