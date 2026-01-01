package recipe_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import recipe_be.dto.request.RecipeRequest;
import recipe_be.dto.response.IngredientItemResponse;
import recipe_be.dto.response.NutritionItemResponse;
import recipe_be.dto.response.RecipeResponse;
import recipe_be.entity.*;
import recipe_be.enums.Role;
import recipe_be.mapper.CategoryMapper;
import recipe_be.mapper.IngredientMapper;
import recipe_be.mapper.NutritionMapper;
import recipe_be.mapper.RecipeMapper;
import recipe_be.repository.RecipeRepository;
import recipe_be.utils.CurrentUserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        String userId = CurrentUserUtils.getUserId();
        User user = userService.getUserById(userId);

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

        return enrichRecipe(save(recipe));
    }

    // ===== CẬP NHẬT CÔNG THỨC =====
    public RecipeResponse updateRecipe(String id, RecipeRequest request) {
        Recipe recipe = getById(id);
        String userId = CurrentUserUtils.getUserId();
        User user = userService.getUserById(userId);

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

        return enrichRecipe(save(recipe));
    }

    // ===== XÓA CÔNG THỨC =====
    public void deleteRecipe(String id) {
        Recipe recipe = getById(id);
        String userId = CurrentUserUtils.getUserId();
        User user = userService.getUserById(userId);
        if (!recipe.getUserId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Bạn không có quyền xóa công thức này");
        }
        if (StringUtils.hasText(recipe.getImage())) {
            imageService.deleteByUrl(recipe.getImage());
        }
        recipeRepository.deleteById(id);
    }

    // ===== THÊM CÔNG THỨC VÀO DANH SÁCH YÊU THÍCH CỦA NGƯỜI DÙNG HIỆN TẠI =====
    public void addFavoriteRecipe(String recipeId) {
        String userId = CurrentUserUtils.getUserId();
        User user = userService.getUserById(userId);

        if (user.getFavoriteRecipes() == null) {
            user.setFavoriteRecipes(new ArrayList<>());
        }

        // Kiểm tra xem đã tồn tại chưa
        boolean exists = user.getFavoriteRecipes().stream().anyMatch(fav -> fav.getRecipeId().equals(recipeId));

        if (exists) {
            throw new RuntimeException("Công thức này đã có trong danh sách yêu thích.");
        }

        user.getFavoriteRecipes().add(new FavoriteRecipe(recipeId));
        userService.save(user);
    }

    // ===== XÓA CÔNG THỨC RA KHỎI DANH SÁCH YÊU THÍCH CỦA NGƯỜI DÙNG HIỆN TẠI =====
    public void removeFavoriteRecipe(String recipeId) {
        String userId = CurrentUserUtils.getUserId();
        User user = userService.getUserById(userId);

        if (user.getFavoriteRecipes() == null || user.getFavoriteRecipes().isEmpty()) {
            throw new RuntimeException("Danh sách yêu thích đang trống.");
        }

        boolean removed = user.getFavoriteRecipes().removeIf(fav -> fav.getRecipeId().equals(recipeId));

        if (!removed) {
            throw new RuntimeException("Công thức không tồn tại trong danh sách yêu thích.");
        }

        userService.save(user);
    }

    // ===== DANH SÁCH TẤT CẢ CÔNG THỨC YÊU THÍCH CỦA NGƯỜI DÙNG HIỊN TẠI =====
    public List<RecipeResponse> getMyFavoriteRecipes() {
        String userId = CurrentUserUtils.getUserId();
        User user = userService.getUserById(userId);

        if (user.getFavoriteRecipes() == null) return List.of();

        return user.getFavoriteRecipes().stream()
                .map(fav -> recipeRepository.findById(fav.getRecipeId())
                        .map(this::enrichRecipe)
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    // ===== LẤY TẤT CẢ =====
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(this::enrichRecipe)
                .toList();
    }

    // ===== LẤY TẤT CẢ CÔNG THỨC THEO CATEGORY =====
    public List<RecipeResponse> getAllRecipesByCategoryId(String categoryId) {
        return recipeRepository.findByCategoryId(categoryId).stream()
                .map(this::enrichRecipe)
                .toList();
    }

    // ===== LẤY THEO NGƯỜI DÙNG =====
    public List<RecipeResponse> getMyRecipes() {
        String userId = CurrentUserUtils.getUserId();
        User user = userService.getUserById(userId);
        return recipeRepository.findByUserId(user.getId()).stream()
                .map(this::enrichRecipe)
                .toList();
    }

    // ===== LẤY THEO ID =====
    public RecipeResponse getRecipeById(String id) {
        Recipe recipe = getById(id);
        return enrichRecipe(recipe);
    }

    // ===== TÌM KIẾM BẰNG TỪ KHÓA =====
    public List<RecipeResponse> searchRecipes(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return List.of();
        }
        List<Recipe> recipes = recipeRepository.findByNameContainingIgnoreCase(keyword);
        return recipeMapper.toRecipeResponseList(recipes);
    }

    public Recipe getById(String id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy công thức."));
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
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
                return new IngredientItemResponse(
                        ingredientMapper.toIngredientResponse(ing),
                        i.getQuantity()
                );
            }).toList());
        }

        // Nutritions
        if (recipe.getNutritions() != null) {
            response.setNutritions(recipe.getNutritions().stream().map(n -> {
                Nutrition nut = nutritionService.getById(n.getNutritionId());
                return new NutritionItemResponse(
                        nutritionMapper.toNutritionResponse(nut),
                        n.getValue()
                );
            }).toList());
        }

        return response;
    }
}