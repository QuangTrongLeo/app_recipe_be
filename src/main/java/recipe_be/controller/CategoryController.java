package recipe_be.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.CategoryRequest;
import recipe_be.dto.response.APIResponse;
import recipe_be.dto.response.CategoryResponse;
import recipe_be.mapper.CategoryMapper;
import recipe_be.service.CategoryService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.recipe.app.url}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public APIResponse createCategory(@RequestBody CategoryRequest request) {
        return APIResponse.builder(categoryService.createCategory(request)).build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public APIResponse updateCategory(@PathVariable String id, @RequestBody CategoryRequest request) {
        return APIResponse.builder(categoryService.updateCategory(id, request)).build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public APIResponse deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return APIResponse.builder("Xóa category thành công!").build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping()
    public APIResponse getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return APIResponse.builder(response).build();
    }
}
