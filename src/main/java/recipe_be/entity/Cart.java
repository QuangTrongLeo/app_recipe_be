package recipe_be.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    private String userId;
    private double totalPrice;
    private boolean isCheckedOut;

    private List<CartItem> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItem {
        private String ingredientId;
        private int quantity;
        private double total;
    }
}
