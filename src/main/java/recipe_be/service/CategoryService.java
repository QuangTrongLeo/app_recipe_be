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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    // ===== TẠO DANH MỤC =====
    public Category createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setStatus(Status.ACTIVE);
        return categoryRepository.save(category);
    }

    // ===== CẬP NHẬT DANH MỤC =====
    public Category updateCategory(String id, CategoryRequest request) {
        Category category = getById(id);
        category.setName(request.getName());
        category.setUpdatedAt(System.currentTimeMillis());
        return categoryRepository.save(category);
    }

    // ===== XÓA DANH MỤC =====
    public void deleteCategory(String id) {
        Category category = getById(id);
        categoryRepository.delete(category);
    }
    
    public Category getById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy category có id: " + id));
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryMapper.toCategoryResponseList(categoryRepository.findAll());
    }
}
