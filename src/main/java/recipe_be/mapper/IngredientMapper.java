package recipe_be.mapper;

import org.mapstruct.Mapper;
import recipe_be.dto.response.IngredientResponse;
import recipe_be.entity.Ingredient;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    IngredientResponse toIngredientResponse(Ingredient ingredient);
    List<IngredientResponse> toIngredientRponseList(List<Ingredient> ingredients);
}
