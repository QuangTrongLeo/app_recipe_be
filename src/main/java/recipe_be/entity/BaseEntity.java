package recipe_be.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import recipe_be.enums.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    @Id
    private String id;
    private Status status;
    private Long createdAt;
    private Long updatedAt;
    
}
