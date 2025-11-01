package recipe_be.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import recipe_be.dto.response.CategoryResponse;
import recipe_be.entity.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "status", target = "status")
    CategoryResponse toCategoryResponse(Category category);
    List<CategoryResponse> toCategoryResponseList(List<Category> categories);
}
