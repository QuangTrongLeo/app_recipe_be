package recipe_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import recipe_be.dto.request.NutritionRequest;
import recipe_be.dto.response.NutritionResponse;
import recipe_be.entity.Nutrition;
import recipe_be.mapper.NutritionMapper;
import recipe_be.repository.NutritionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NutritionService {

    private final NutritionRepository nutritionRepository;
    private final NutritionMapper nutritionMapper;

    // ===== TẠO NUTRITION =====
    public NutritionResponse createNutrition(NutritionRequest request) {
        Nutrition nutrition = Nutrition.builder()
                .name(request.getName())
                .unit(request.getUnit())
                .type(request.getType())
                .build();
        return nutritionMapper.toNutritionResponse(nutritionRepository.save(nutrition));
    }

    // ===== CẬP NHẬT NUTRITION =====
    public NutritionResponse updateNutrition(String id, NutritionRequest request) {
        Nutrition existing = getById(id);

        if (request.getType() != null) {
            existing.setName(request.getName());
        }
        if (StringUtils.hasText(request.getUnit())) {
            existing.setUnit(request.getUnit());
        }
        if (StringUtils.hasText(request.getName())) {
            existing.setType(request.getType());
        }

        return nutritionMapper.toNutritionResponse(nutritionRepository.save(existing));
    }

    // ===== XÓA NUTRITION =====
    public void deleteNutrition(String id) {
        if (!nutritionRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy nutrition với id: " + id);
        }
        nutritionRepository.deleteById(id);
    }

    // ===== DANH SÁCH NUTRITION =====
    public List<NutritionResponse> getAllNutrition() {
        return nutritionMapper.toNutritionResponseList(nutritionRepository.findAll());
    }

    // ===== LẤY NUTRITION =====
    public NutritionResponse getNutritionById(String id) {
        Nutrition nutrition = getById(id);
        return nutritionMapper.toNutritionResponse(nutrition);
    }

    // ===== Lấy Nutrition bằng ID =====
    public Nutrition getById(String id){
        return nutritionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nutrition với id: " + id));
    }
}
