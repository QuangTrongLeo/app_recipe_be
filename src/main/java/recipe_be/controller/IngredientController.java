package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.IngredientRequest;
import recipe_be.dto.response.ApiResponse;
import recipe_be.dto.response.IngredientResponse;
import recipe_be.service.IngredientService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ApiResponse<IngredientResponse> createIngredient(@ModelAttribute IngredientRequest request) {
        IngredientResponse response = ingredientService.createIngredient(request);
        return ApiResponse.<IngredientResponse>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<IngredientResponse> updateIngredient(@PathVariable String id,
                                        @ModelAttribute IngredientRequest request) {
        IngredientResponse response = ingredientService.updateIngredient(id, request);
        return ApiResponse.<IngredientResponse>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteIngredient(@PathVariable String id) {
        ingredientService.deleteIngredient(id);
        return ApiResponse.<Boolean>builder()
                .result(true)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public ApiResponse<List<IngredientResponse>> getAllIngredients() {
        List<IngredientResponse> response = ingredientService.getAllIngredients();
        return ApiResponse.<List<IngredientResponse>>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ApiResponse<IngredientResponse> getIngredientById(@PathVariable String id) {
        IngredientResponse response = ingredientService.getIngredientById(id);
        return ApiResponse.<IngredientResponse>builder()
                .result(response)
                .build();
    }
}
