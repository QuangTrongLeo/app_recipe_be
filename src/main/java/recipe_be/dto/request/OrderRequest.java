package recipe_be.dto.request;

import lombok.Data;
import recipe_be.enums.OrderStatus;
import recipe_be.enums.PaymentMethod;

@Data
public class OrderRequest {
    String userId;
    String recipeId;
    PaymentMethod paymentMethod;
    OrderStatus orderStatus;
}
