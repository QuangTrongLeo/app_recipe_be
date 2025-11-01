package recipe_be.mapper;

import org.mapstruct.Mapper;
import recipe_be.dto.response.NutritionResponse;
import recipe_be.entity.Nutrition;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NutritionMapper {
    NutritionResponse toNutritionResponse(Nutrition nutrition);
    List<NutritionResponse> toNutritionResponseList(List<Nutrition> nutritions);
}
