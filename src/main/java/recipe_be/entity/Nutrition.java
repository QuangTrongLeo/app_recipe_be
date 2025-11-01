package recipe_be.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import recipe_be.enums.NutritionType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "nutrition")
public class Nutrition {
    @Id
    private String id;
    private String name;
    private String unit;
    private NutritionType type;
}
