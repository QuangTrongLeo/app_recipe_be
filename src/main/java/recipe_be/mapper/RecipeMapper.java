package recipe_be.mapper;

import org.mapstruct.Mapper;
import recipe_be.dto.response.RecipeResponse;
import recipe_be.entity.Recipe;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeResponse toRecipeResponse(Recipe recipe);
    List<RecipeResponse> toRecipeResponseList(List<Recipe> recipes);
}


