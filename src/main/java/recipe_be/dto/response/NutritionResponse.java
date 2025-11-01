package recipe_be.dto.response;

import lombok.Data;

@Data
public class NutritionResponse {
    private String id;
    private String name;
    private String unit;
    private String type;
}
