package recipe_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import recipe_be.dto.request.CategoryRequest;
import recipe_be.dto.response.CategoryResponse;
import recipe_be.entity.Category;
import recipe_be.entity.User;
import recipe_be.enums.Role;
import recipe_be.enums.Status;
import recipe_be.mapper.CategoryMapper;
import recipe_be.repository.CategoryRepository;
import recipe_be.utils.CurrentUserUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public Category createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setStatus(Status.ACTIVE);
        return categoryRepository.save(category);
    }
    
    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryMapper.toCategoryResponseList(categoryRepository.findAll());
    }
}
