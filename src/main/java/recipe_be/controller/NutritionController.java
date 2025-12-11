package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.NutritionRequest;
import recipe_be.dto.response.APIResponse;
import recipe_be.dto.response.NutritionResponse;
import recipe_be.enums.NutritionType;
import recipe_be.service.NutritionService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.recipe.app.url}/nutrition")
public class NutritionController {
    private final NutritionService nutritionService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public APIResponse createNutrition(@RequestBody NutritionRequest request) {
        NutritionResponse response = nutritionService.createNutrition(request);
        return APIResponse.builder(response).build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public APIResponse updateNutrition(@PathVariable String id, @RequestBody NutritionRequest request) {
        NutritionResponse response = nutritionService.updateNutrition(id, request);
        return APIResponse.builder(response).build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public APIResponse deleteNutrition(@PathVariable String id) {
        nutritionService.deleteNutrition(id);
        return APIResponse.builder("Xóa nutrition thành công!").build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public APIResponse getAllNutrition() {
        List<NutritionResponse> response = nutritionService.getAllNutrition();
        return APIResponse.builder(response).build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public APIResponse getNutritionById(@PathVariable String id) {
        NutritionResponse response = nutritionService.getNutritionById(id);
        return APIResponse.builder(response).build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/type")
    public APIResponse getAllNutritionType() {
        List<String> types = nutritionService.getAllNutritionTypes();
        return APIResponse.builder(types).build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/type/{typeName}")
    public APIResponse getNutritionTypeByName(@PathVariable String typeName) {
        NutritionType type = nutritionService.getNutritionTypeByName(typeName);
        return APIResponse.builder(type).build();
    }
}
