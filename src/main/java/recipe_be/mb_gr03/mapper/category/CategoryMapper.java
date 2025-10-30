package recipe_be.mb_gr03.mapper.category;

import org.springframework.stereotype.Component;
import recipe_be.mb_gr03.dto.response.category.CategoryResponse;
import recipe_be.mb_gr03.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(category.getName().toValue());
    }

    public List<CategoryResponse> toCategoryResponseList(List<Category> categories) {
        return categories.stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }
}
