package recipe_be.dto.response.cart;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartResponse {
    String userId; 
    List<CartItemResponse> items;
    
}
