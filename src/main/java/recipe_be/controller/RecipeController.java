package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.RecipeRequest;
import recipe_be.dto.response.ApiResponse;
import recipe_be.dto.response.RecipeResponse;

import recipe_be.service.RecipeService;

import java.util.List;

@RequiredArgsConstructor
@RestController
    @RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping
    public ApiResponse<RecipeResponse> createRecipe(@ModelAttribute RecipeRequest request) {
        RecipeResponse response = recipeService.createRecipe(request);
        return ApiResponse.<RecipeResponse>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ApiResponse<RecipeResponse> updateRecipe(@PathVariable String id,
                                    @ModelAttribute RecipeRequest request) {
        RecipeResponse response = recipeService.updateRecipe(id, request);
        return ApiResponse.<RecipeResponse>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteRecipe(@PathVariable String id) {
        recipeService.deleteRecipe(id);
        return ApiResponse.<Boolean>builder()
                .result(true)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public ApiResponse<List<RecipeResponse>> getAllRecipes() {
        List<RecipeResponse> response = recipeService.getAllRecipes();
        return ApiResponse.<List<RecipeResponse>>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{categoryId}/category")
    public ApiResponse<List<RecipeResponse>> getAllRecipesByCategoryId(@PathVariable String categoryId) {
        List<RecipeResponse> response = recipeService.getAllRecipesByCategoryId(categoryId);
        return ApiResponse.<List<RecipeResponse>>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/me")
    public ApiResponse<List<RecipeResponse>> getMyRecipes() {
        List<RecipeResponse> response = recipeService.getMyRecipes();
        return ApiResponse.<List<RecipeResponse>>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ApiResponse<RecipeResponse> getRecipeById(@PathVariable String id) {
        RecipeResponse response = recipeService.getRecipeById(id);
        return ApiResponse.<RecipeResponse>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/{id}/favorite")
    public ApiResponse<Boolean> addFavoriteRecipe(@PathVariable String id) {
        recipeService.addFavoriteRecipe(id);
        return ApiResponse.<Boolean>builder()
                .result(true)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/{id}/favorite")
    public ApiResponse<Boolean> removeFavoriteRecipe(@PathVariable String id) {
        recipeService.removeFavoriteRecipe(id);
        return ApiResponse.<Boolean>builder()
                .result(true)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/favorites")
    public ApiResponse<List<RecipeResponse>> getMyFavoriteRecipes() {
        List<RecipeResponse> response = recipeService.getMyFavoriteRecipes();
        return ApiResponse.<List<RecipeResponse>>builder()
                .result(response)
                .build();
    }
}
