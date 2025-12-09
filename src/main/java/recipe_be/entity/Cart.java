package recipe_be.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class Cart extends  BaseEntity {
    private String userId;
    private double totalPrice;
    private boolean isCheckedOut;
    @DBRef
    private List<CartItem> items;
}
