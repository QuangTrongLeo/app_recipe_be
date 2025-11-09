package recipe_be.mapper;

import org.mapstruct.Mapper;
import recipe_be.dto.response.IngredientResponse;
import recipe_be.entity.Ingredient;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    IngredientResponse toNutritionIngredient(Ingredient ingredient);
    List<IngredientResponse> toNutritionIngredientList(List<Ingredient> ingredients);
}
