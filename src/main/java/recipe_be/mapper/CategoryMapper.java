package recipe_be.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import recipe_be.dto.response.CategoryResponse;
import recipe_be.entity.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "status", source = "status")
    CategoryResponse toCategoryResponse(Category category);
    List<CategoryResponse> toCategoryResponseList(List<Category> categories);
}
