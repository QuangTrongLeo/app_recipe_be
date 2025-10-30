package recipe_be.mb_gr03.controller.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipe_be.mb_gr03.dto.response.ApiResp;
import recipe_be.mb_gr03.dto.response.category.CategoryResponse;
import recipe_be.mb_gr03.service.category.CategoryService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.recipe.app.url}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    // ===== ALL CATEGORY =====
    @GetMapping()
    public ResponseEntity<ApiResp<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> responses = categoryService.getAllCategories();
        ApiResp<List<CategoryResponse>> apiResp = new ApiResp<>(HttpStatus.OK.value(), responses, "Danh sách Category");
        return ResponseEntity.ok(apiResp);
    }
}
