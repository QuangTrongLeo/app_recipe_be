package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.RecipeRequest;
import recipe_be.dto.response.APIResponse;
import recipe_be.dto.response.RecipeResponse;

import recipe_be.service.RecipeService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.recipe.app.url}/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public APIResponse createRecipe(@ModelAttribute RecipeRequest request) {
        RecipeResponse response = recipeService.createRecipe(request);
        return APIResponse.builder(response).build();
    }

    @PutMapping("/{id}")
    public APIResponse updateRecipe(@PathVariable String id,
                                    @ModelAttribute RecipeRequest request) {
        RecipeResponse response = recipeService.updateRecipe(id, request);
        return APIResponse.builder(response).build();
    }

    @DeleteMapping("/{id}")
    public APIResponse deleteRecipe(@PathVariable String id) {
        recipeService.deleteRecipe(id);
        return APIResponse.builder("Xóa công thức thành công").build();
    }

    @GetMapping
    public APIResponse getAllRecipes() {
        List<RecipeResponse> response = recipeService.getAllRecipes();
        return APIResponse.builder(response).build();
    }

    @GetMapping("/category/{categoryId}")
    public APIResponse getAllRecipesByCategoryId(@PathVariable String categoryId) {
        List<RecipeResponse> response = recipeService.getAllRecipesByCategoryId(categoryId);
        return APIResponse.builder(response).build();
    }

    @GetMapping("/me")
    public APIResponse getMyRecipes() {
        List<RecipeResponse> response = recipeService.getMyRecipes();
        return APIResponse.builder(response).build();
    }

    @GetMapping("/{id}")
    public APIResponse getRecipeById(@PathVariable String id) {
        RecipeResponse response = recipeService.getRecipeById(id);
        return APIResponse.builder(response).build();
    }
}
