package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.IngredientRequest;
import recipe_be.dto.response.APIResponse;
import recipe_be.dto.response.IngredientResponse;
import recipe_be.service.IngredientService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.recipe.app.url}/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public APIResponse createIngredient(@ModelAttribute IngredientRequest request) {
        IngredientResponse response = ingredientService.createIngredient(request);
        return APIResponse.builder(response).build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public APIResponse updateIngredient(@PathVariable String id,
                                        @ModelAttribute IngredientRequest request) {
        IngredientResponse response = ingredientService.updateIngredient(id, request);
        return APIResponse.builder(response).build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public APIResponse deleteIngredient(@PathVariable String id) {
        ingredientService.deleteIngredient(id);
        return APIResponse.builder("Xóa nguyên liệu thành công!").build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public APIResponse getAllIngredients() {
        List<IngredientResponse> response = ingredientService.getAllIngredients();
        return APIResponse.builder(response).build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public APIResponse getIngredientById(@PathVariable String id) {
        IngredientResponse response = ingredientService.getIngredientById(id);
        return APIResponse.builder(response).build();
    }
}
