package recipe_be.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import recipe_be.dto.response.IngredientResponse;
import recipe_be.entity.Image;
import recipe_be.entity.Ingredient;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    @Mapping(source = "image.url", target = "image")
    IngredientResponse toIngredientResponse(Ingredient ingredient);

    List<IngredientResponse> toIngredientResponseList(List<Ingredient> ingredients);
}
