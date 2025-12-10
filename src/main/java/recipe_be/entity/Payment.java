package recipe_be.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import recipe_be.enums.PaymentMethod;
import recipe_be.enums.Status;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment extends BaseEntity {
    
    private String orderId;
    private String userId;
    private double amount;
    private PaymentMethod paymentMethod;
//    private Status status;
    private String transactionId;
    private Date paidAt;
}

