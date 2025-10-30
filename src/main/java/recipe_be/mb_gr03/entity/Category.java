package recipe_be.mb_gr03.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import recipe_be.mb_gr03.enums.EnumCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "categories")
public class Category {
    @Id
    private String id;

    private EnumCategory name;
}
