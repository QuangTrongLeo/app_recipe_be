package recipe_be.dto.request;

import lombok.Data;
import recipe_be.enums.NutritionType;

@Data
public class NutritionRequest {
    private String name;
    private String unit;
    private NutritionType type;
}
