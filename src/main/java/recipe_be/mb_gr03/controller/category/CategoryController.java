package recipe_be.mb_gr03.controller.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
