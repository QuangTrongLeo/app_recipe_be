package recipe_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import recipe_be.dto.request.IngredientRequest;
import recipe_be.dto.response.IngredientResponse;
import recipe_be.entity.Ingredient;
import recipe_be.entity.Image;
import recipe_be.mapper.IngredientMapper;
import recipe_be.repository.IngredientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    private final ImageService imageService;

    //  ===== TẠO NGUYÊN LIỆU =====
    public IngredientResponse createIngredient(IngredientRequest request) {
        Ingredient ingredient = Ingredient.builder()
                .name(request.getName())
                .unit(request.getUnit())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();

        MultipartFile imageFile = request.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = imageService.uploadAndSave(imageFile);
            ingredient.setImage(image.getUrl());
        }

        return ingredientMapper.toNutritionIngredient(ingredientRepository.save(ingredient));
    }

    //  ===== CẬP NHẬT NGUYÊN LIỆU =====
    public IngredientResponse updateIngredient(String id, IngredientRequest request) {
        // Lấy ingredient từ DB
        Ingredient ingredient = getById(id);

        // Cập nhật chỉ những field có dữ liệu
        if (StringUtils.hasText(request.getName())) {
            ingredient.setName(request.getName());
        }
        if (StringUtils.hasText(request.getUnit())) {
            ingredient.setUnit(request.getUnit());
        }
        if (request.getPrice() > 0) { // chỉ cập nhật nếu giá > 0
            ingredient.setPrice(request.getPrice());
        }
        if (request.getStock() >= 0) { // chỉ cập nhật nếu stock >= 0
            ingredient.setStock(request.getStock());
        }

        MultipartFile imageFile = request.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            if (StringUtils.hasText(ingredient.getImage())) {
                imageService.deleteByUrl(ingredient.getImage());
            }
            Image image = imageService.uploadAndSave(imageFile);
            ingredient.setImage(image.getUrl());
        }

        // Lưu và trả về response
        Ingredient saved = ingredientRepository.save(ingredient);
        return ingredientMapper.toNutritionIngredient(saved);
    }


    //  ===== LẤY NGUYÊN LIỆU BẰNG ID =====
    public IngredientResponse getIngredientById(String id) {
        Ingredient ingredient = getById(id);
        return ingredientMapper.toNutritionIngredient(ingredient);
    }

    //  ===== LẤY TẤT CẢ NGUYÊN LIỆU =====
    public List<IngredientResponse> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredientMapper.toNutritionIngredientList(ingredients);
    }

    //  ===== XÓA NGUYÊN LIỆU =====
    public void deleteIngredient(String id) {
        Ingredient ingredient = getById(id);

        if (ingredient.getImage() != null) {
            imageService.deleteByUrl(ingredient.getImage());
        }

        ingredientRepository.deleteById(id);
    }

    // Lấy nguyên liệu bằng ID
    private Ingredient getById(String id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nguyên liệu"));
    }
}
