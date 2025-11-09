package recipe_be.dto.response;

import lombok.Data;

@Data
public class IngredientResponse {
    private String id;
    private String name;
    private String image;
    private String unit;
    private double price;
    private double stock;
}
