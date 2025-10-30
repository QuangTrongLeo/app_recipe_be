package recipe_be.mb_gr03.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import recipe_be.mb_gr03.enums.EnumPayment;
import recipe_be.mb_gr03.enums.EnumStatus;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String orderId;
    private String userId;
    private double amount;
    private EnumPayment paymentMethod;
    private EnumStatus status;
    private String transactionId;
    private Date paidAt;
}

