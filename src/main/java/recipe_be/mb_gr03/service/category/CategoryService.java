package recipe_be.mb_gr03.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import recipe_be.mb_gr03.dto.response.category.CategoryResponse;
import recipe_be.mb_gr03.mapper.category.CategoryMapper;
import recipe_be.mb_gr03.repository.category.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllCategories() {
        return categoryMapper.toCategoryResponseList(categoryRepository.findAll());
    }
}
