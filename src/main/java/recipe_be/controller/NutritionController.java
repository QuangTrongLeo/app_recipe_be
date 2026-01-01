package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.NutritionRequest;
import recipe_be.dto.response.ApiResponse;
import recipe_be.dto.response.NutritionResponse;
import recipe_be.enums.NutritionType;
import recipe_be.service.NutritionService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/nutrition")
public class NutritionController {
    private final NutritionService nutritionService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ApiResponse<NutritionResponse> createNutrition(@RequestBody NutritionRequest request) {
        NutritionResponse response = nutritionService.createNutrition(request);
        return ApiResponse.<NutritionResponse>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<NutritionResponse> updateNutrition(@PathVariable String id, @RequestBody NutritionRequest request) {
        NutritionResponse response = nutritionService.updateNutrition(id, request);
        return ApiResponse.<NutritionResponse>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteNutrition(@PathVariable String id) {
        nutritionService.deleteNutrition(id);
        return ApiResponse.<Boolean>builder()
                .result(true).build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public ApiResponse<List<NutritionResponse>> getAllNutrition() {
        List<NutritionResponse> response = nutritionService.getAllNutrition();
        return ApiResponse.<List<NutritionResponse>>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ApiResponse<NutritionResponse> getNutritionById(@PathVariable String id) {
        NutritionResponse response = nutritionService.getNutritionById(id);
        return ApiResponse.<NutritionResponse>builder()
                .result(response)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/type")
    public ApiResponse<List<String>> getAllNutritionType() {
        List<String> types = nutritionService.getAllNutritionTypes();
        return ApiResponse.<List<String>>builder()
                .result(types)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/type/{typeName}")
    public ApiResponse<NutritionType> getNutritionTypeByName(@PathVariable String typeName) {
        NutritionType type = nutritionService.getNutritionTypeByName(typeName);
        return ApiResponse.<NutritionType>builder()
                .result(type)
                .build();
    }
}
