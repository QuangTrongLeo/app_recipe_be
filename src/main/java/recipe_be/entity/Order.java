package recipe_be.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import recipe_be.enums.OrderStatus;
import recipe_be.enums.PaymentMethod;
import recipe_be.enums.PaymentStatus;
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
    private OrderStatus status;
    private Date createdAt;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private List<OrderItem> orderItems;
}

