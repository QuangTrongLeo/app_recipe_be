package recipe_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import recipe_be.dto.request.RecipeRequest;
import recipe_be.dto.response.RecipeResponse;
import recipe_be.entity.*;
import recipe_be.enums.Role;
import recipe_be.mapper.CategoryMapper;
import recipe_be.mapper.IngredientMapper;
import recipe_be.mapper.NutritionMapper;
import recipe_be.mapper.RecipeMapper;
import recipe_be.repository.CategoryRepository;
import recipe_be.repository.IngredientRepository;
import recipe_be.repository.NutritionRepository;
import recipe_be.repository.RecipeRepository;
import recipe_be.utils.CurrentUserUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;
    private final CategoryMapper categoryMapper;
    private final IngredientMapper ingredientMapper;
    private final NutritionMapper nutritionMapper;

    private final ImageService imageService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final IngredientService ingredientService;
    private final NutritionService nutritionService;

    // ===== TẠO CÔNG THỨC =====
    public RecipeResponse createRecipe(RecipeRequest request) {
        String email = CurrentUserUtils.getEmail();
        User user = userService.getUserByEmail(email);

        Recipe recipe = Recipe.builder()
                .userId(user.getId())
                .categoryId(request.getCategoryId())
                .name(request.getName())
                .description(request.getDescription())
                .instructions(request.getInstructions())
                .time(request.getTime())
                .ingredients(request.getIngredients())
                .nutritions(request.getNutritions())
                .build();

        MultipartFile imageFile = request.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = imageService.uploadAndSave(imageFile);
            recipe.setImage(image.getUrl());
        }

        return enrichRecipe(recipeRepository.save(recipe));
    }

    // ===== CẬP NHẬT CÔNG THỨC =====
    public RecipeResponse updateRecipe(String id, RecipeRequest request) {
        Recipe recipe = getById(id);

        String email = CurrentUserUtils.getEmail();
        User user = userService.getUserByEmail(email);

        if (!recipe.getUserId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa công thức này");
        }

        if (StringUtils.hasText(request.getName())) recipe.setName(request.getName());
        if (StringUtils.hasText(request.getDescription())) recipe.setDescription(request.getDescription());
        if (StringUtils.hasText(request.getInstructions())) recipe.setInstructions(request.getInstructions());
        if (request.getTime() > 0) recipe.setTime(request.getTime());
        if (StringUtils.hasText(request.getCategoryId())) recipe.setCategoryId(request.getCategoryId());
        if (request.getIngredients() != null) recipe.setIngredients(request.getIngredients());
        if (request.getNutritions() != null) recipe.setNutritions(request.getNutritions());

        MultipartFile imageFile = request.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            if (StringUtils.hasText(recipe.getImage())) {
                imageService.deleteByUrl(recipe.getImage());
            }
            Image newImage = imageService.uploadAndSave(imageFile);
            recipe.setImage(newImage.getUrl());
        }

        return enrichRecipe(recipeRepository.save(recipe));
    }

    // ===== XÓA CÔNG THỨC =====
    public void deleteRecipe(String id) {
        Recipe recipe = getById(id);
        String email = CurrentUserUtils.getEmail();
        User user = userService.getUserByEmail(email);
        if (!recipe.getUserId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Bạn không có quyền xóa công thức này");
        }
        if (StringUtils.hasText(recipe.getImage())) {
            imageService.deleteByUrl(recipe.getImage());
        }
        recipeRepository.deleteById(id);
    }

    // ===== LẤY TẤT CẢ =====
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(this::enrichRecipe)
                .toList();
    }

    // ===== LẤY THEO NGƯỜI DÙNG =====
    public List<RecipeResponse> getMyRecipes() {
        String email = CurrentUserUtils.getEmail();
        User user = userService.getUserByEmail(email);
        return recipeRepository.findByUserId(user.getId()).stream()
                .map(this::enrichRecipe)
                .toList();
    }

    // ===== LẤY THEO ID =====
    public RecipeResponse getRecipeById(String id) {
        Recipe recipe = getById(id);
        return enrichRecipe(recipe);
    }

    private Recipe getById(String id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy công thức."));
    }

    // ===== HÀM HỖ TRỢ GẮN CATEGORY / INGREDIENT / NUTRITION =====
    private RecipeResponse enrichRecipe(Recipe recipe) {
        RecipeResponse response = recipeMapper.toRecipeResponse(recipe);

        // Category
        Category category = categoryService.getById(recipe.getCategoryId());
        response.setCategory(categoryMapper.toCategoryResponse(category));

        // Ingredients
        if (recipe.getIngredients() != null) {
            response.setIngredients(recipe.getIngredients().stream().map(i -> {
                Ingredient ing = ingredientService.getById(i.getIngredientId());
                return new RecipeResponse.IngredientItemResponse(
                        ingredientMapper.toIngredientResponse(ing),
                        i.getQuantity()
                );
            }).toList());
        }

        // Nutritions
        if (recipe.getNutritions() != null) {
            response.setNutritions(recipe.getNutritions().stream().map(n -> {
                Nutrition nut = nutritionService.getById(n.getNutritionId());
                return new RecipeResponse.NutritionItemResponse(
                        nutritionMapper.toNutritionResponse(nut),
                        n.getValue()
                );
            }).toList());
        }

        return response;
    }
}