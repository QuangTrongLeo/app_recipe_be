package recipe_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientRequest {
    private String name;
    private String unit;
    private double price;
    private double stock;
    private MultipartFile image;
}
