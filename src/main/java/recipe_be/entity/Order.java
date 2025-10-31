package recipe_be.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import recipe_be.enums.Status;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String userId;
    private double totalPrice;
    private Status status;
    private Date createdAt;

    private List<OrderItem> items;
    private Shipping shipping;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItem {
        private String ingredientId;
        private int quantity;
        private double total;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Shipping {
        private String address;
        private String phone;
    }
}

