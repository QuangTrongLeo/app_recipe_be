package recipe_be.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import recipe_be.dto.response.IngredientResponse;
import recipe_be.entity.Image;
import recipe_be.entity.Ingredient;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    @Mapping(target = "image", source = "image")
    IngredientResponse toIngredientResponse(Ingredient ingredient);
    List<IngredientResponse> toIngredientRponseList(List<Ingredient> ingredients);
    default String map(Image image) {
        return image == null ? null : image.getUrl();
    }
}
