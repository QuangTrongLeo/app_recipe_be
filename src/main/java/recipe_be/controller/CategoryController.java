package recipe_be.controller;

import lombok.RequiredArgsConstructor;

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
    private final CategoryMapper categoryMapper;
    
    @PostMapping()
    public APIResponse createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse response = categoryMapper.toCategoryResponse(categoryService.create(request));
        return  APIResponse.builder(response).build();
    }
    
    @GetMapping()
    public APIResponse getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return  APIResponse.builder(response).build();
    }
}
