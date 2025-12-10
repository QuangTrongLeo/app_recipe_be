package recipe_be.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionItem {
    private String nutritionId;
    private double value;
}

