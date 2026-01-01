package recipe_be.mapper;

import org.mapstruct.*;
import recipe_be.dto.response.RecipeResponse;
import recipe_be.entity.Recipe;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    @Named("toFull")
    RecipeResponse toRecipeResponse(Recipe recipe);
    @IterableMapping(qualifiedByName = "toFull")
    List<RecipeResponse> toRecipeResponseList(List<Recipe> recipes);
    
    @Mapping(target = "ingredients", ignore = true)
    @Mapping(target = "nutritions", ignore = true)
    @Named("toShort")
    RecipeResponse toShortRecipeResponse(Recipe recipe);
    
    RecipeResponse toResponse(String id);
}


