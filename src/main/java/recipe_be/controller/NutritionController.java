package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.NutritionRequest;
import recipe_be.dto.response.APIResponse;
import recipe_be.dto.response.NutritionResponse;
import recipe_be.service.NutritionService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.recipe.app.url}/nutrition")
public class NutritionController {
    private final NutritionService nutritionService;

    @PostMapping
    public APIResponse createNutrition(@RequestBody NutritionRequest request) {
        NutritionResponse response = nutritionService.createNutrition(request);
        return APIResponse.builder(response).build();
    }

    @PutMapping("/{id}")
    public APIResponse updateNutrition(@PathVariable String id, @RequestBody NutritionRequest request) {
        NutritionResponse response = nutritionService.updateNutrition(id, request);
        return APIResponse.builder(response).build();
    }

    @DeleteMapping("/{id}")
    public APIResponse deleteNutrition(@PathVariable String id) {
        nutritionService.deleteNutrition(id);
        return APIResponse.builder("Xóa nutrition thành công!").build();
    }

    @GetMapping
    public APIResponse getAllNutrition() {
        List<NutritionResponse> response = nutritionService.getAllNutrition();
        return APIResponse.builder(response).build();
    }

    @GetMapping("/{id}")
    public APIResponse getNutritionById(@PathVariable String id) {
        NutritionResponse response = nutritionService.getNutritionById(id);
        return APIResponse.builder(response).build();
    }
}
