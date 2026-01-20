package recipe_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import recipe_be.dto.request.RecipeRequest;
import recipe_be.dto.response.*;
import recipe_be.entity.*;
import recipe_be.enums.ErrorCode;
import recipe_be.enums.Role;
import recipe_be.exception.AppException;
import recipe_be.mapper.CategoryMapper;
import recipe_be.mapper.IngredientMapper;
import recipe_be.mapper.NutritionMapper;
import recipe_be.mapper.RecipeMapper;
import recipe_be.repository.CategoryRepository;
import recipe_be.repository.IngredientRepository;
import recipe_be.repository.NutritionRepository;
import recipe_be.repository.RecipeRepository;
import recipe_be.utils.CurrentUserUtils;

import java.sql.Struct;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;
    private final CategoryMapper categoryMapper;
    private final IngredientMapper ingredientMapper;
    private final NutritionMapper nutritionMapper;
    private final NutritionRepository nutritionRepository;
    private final ImageService imageService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final IngredientService ingredientService;
    private final IngredientRepository ingredientRepository;
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
            throw new AppException(ErrorCode.ACCESS_DENIED);
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
            throw new AppException(ErrorCode.ACCESS_DENIED);
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
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        user.getFavoriteRecipes().add(new FavoriteRecipe(recipeId));
        userService.save(user);
    }

    // ===== XÓA CÔNG THỨC RA KHỎI DANH SÁCH YÊU THÍCH CỦA NGƯỜI DÙNG HIỆN TẠI =====
    public void removeFavoriteRecipe(String recipeId) {
        String userId = CurrentUserUtils.getUserId();
        User user = userService.getUserById(userId);

        if (user.getFavoriteRecipes() == null || user.getFavoriteRecipes().isEmpty()) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }

        boolean removed = user.getFavoriteRecipes().removeIf(fav -> fav.getRecipeId().equals(recipeId));

        if (!removed) {
            throw new AppException(ErrorCode.NOT_FOUND);
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
    public Page<RecipeResponse> getAllRecipes(Pageable pageable) {
        Page<Recipe> recipes = recipeRepository.findAll(pageable);
        List<RecipeResponse> responses = new ArrayList<>();
        // Categories
        Set<String> categoryIds = recipes.getContent().stream()
                .map(Recipe::getCategoryId)
                .collect(Collectors.toSet());
        
        Map<String, CategoryResponse> categoryMap =
                categoryRepository.findAllById(categoryIds).stream()
                        .map(categoryMapper::toCategoryResponse)
                        .collect(Collectors.toMap(CategoryResponse::getId, Function.identity()));
        
        // Ingredients
        Set<String> ingredientIds = recipes.getContent().stream()
                .flatMap(recipe -> recipe.getIngredients().stream().map(IngredientItem::getIngredientId))
                .collect(Collectors.toSet());
        
        Map<String ,IngredientResponse> ingredientMap = ingredientRepository.findByIdIn(ingredientIds).stream()
                .map(ingredientMapper::toIngredientResponse)
                .collect(Collectors.toMap(
                        IngredientResponse::getId, Function.identity()
                ));
        
        // Nutritions
        Set<String> nutritionIds = recipes.getContent().stream()
                .flatMap(recipe -> recipe.getNutritions().stream().map(NutritionItem::getNutritionId))
                .collect(Collectors.toSet());
        
        Map<String, NutritionResponse> nutritionMap = nutritionRepository.findByIdIn(nutritionIds).stream()
                .map(nutritionMapper::toNutritionResponse)
                .collect(Collectors.toMap(
                        NutritionResponse::getId, Function.identity()
                ));
        
        
        recipes.getContent().forEach(recipe -> {
            RecipeResponse recipeResponse = recipeMapper.toRecipeResponse(recipe);
            recipeResponse.setCategory(categoryMap.getOrDefault(recipe.getCategoryId(), null));
            
            List<IngredientItemResponse> ingredientItemResponses = new ArrayList<>();
            recipe.getIngredients().forEach(ingredient -> {
                IngredientItemResponse ingredientItemResponse = new IngredientItemResponse();
                ingredientItemResponse.setQuantity(ingredient.getQuantity());
                ingredientItemResponse.setIngredient(ingredientMap.getOrDefault(ingredient.getIngredientId(), null));
                ingredientItemResponses.add(ingredientItemResponse);
            });
            recipeResponse.setIngredients(ingredientItemResponses);
            
            List<NutritionItemResponse> nutritionItemResponses = new ArrayList<>();
            recipe.getNutritions().forEach(nutrition -> {
                NutritionItemResponse nutritionItemResponse = new NutritionItemResponse();
                nutritionItemResponse.setValue(nutrition.getValue());
                nutritionItemResponse.setNutrition(nutritionMap.getOrDefault(nutrition.getNutritionId(), null));
                nutritionItemResponses.add(nutritionItemResponse);
            });
            recipeResponse.setNutritions(nutritionItemResponses);
            
            responses.add(recipeResponse);
        });

        Page<RecipeResponse> responsePage =
                new PageImpl<>(responses, pageable, recipes.getTotalElements());
        
        return responsePage;
    }

    // ===== LẤY TẤT CẢ CÔNG THỨC THEO CATEGORY =====
    public Page<RecipeResponse> getAllRecipesByCategoryId(String categoryId, Pageable pageable) {
        Page<Recipe> recipes = recipeRepository.findByCategoryId(categoryId, pageable);
        List<RecipeResponse> responses = new ArrayList<>();
        // Categories
        Set<String> categoryIds = recipes.getContent().stream()
                .map(Recipe::getCategoryId)
                .collect(Collectors.toSet());

        Map<String, CategoryResponse> categoryMap =
                categoryRepository.findAllById(categoryIds).stream()
                        .map(categoryMapper::toCategoryResponse)
                        .collect(Collectors.toMap(CategoryResponse::getId, Function.identity()));

        // Ingredients
        Set<String> ingredientIds = recipes.getContent().stream()
                .flatMap(recipe -> recipe.getIngredients().stream().map(IngredientItem::getIngredientId))
                .collect(Collectors.toSet());

        Map<String ,IngredientResponse> ingredientMap = ingredientRepository.findByIdIn(ingredientIds).stream()
                .map(ingredientMapper::toIngredientResponse)
                .collect(Collectors.toMap(
                        IngredientResponse::getId, Function.identity()
                ));

        // Nutritions
        Set<String> nutritionIds = recipes.getContent().stream()
                .flatMap(recipe -> recipe.getNutritions().stream().map(NutritionItem::getNutritionId))
                .collect(Collectors.toSet());

        Map<String, NutritionResponse> nutritionMap = nutritionRepository.findByIdIn(nutritionIds).stream()
                .map(nutritionMapper::toNutritionResponse)
                .collect(Collectors.toMap(
                        NutritionResponse::getId, Function.identity()
                ));


        recipes.getContent().forEach(recipe -> {
            RecipeResponse recipeResponse = recipeMapper.toRecipeResponse(recipe);
            recipeResponse.setCategory(categoryMap.getOrDefault(recipe.getCategoryId(), null));

            List<IngredientItemResponse> ingredientItemResponses = new ArrayList<>();
            recipe.getIngredients().forEach(ingredient -> {
                IngredientItemResponse ingredientItemResponse = new IngredientItemResponse();
                ingredientItemResponse.setQuantity(ingredient.getQuantity());
                ingredientItemResponse.setIngredient(ingredientMap.getOrDefault(ingredient.getIngredientId(), null));
                ingredientItemResponses.add(ingredientItemResponse);
            });
            recipeResponse.setIngredients(ingredientItemResponses);

            List<NutritionItemResponse> nutritionItemResponses = new ArrayList<>();
            recipe.getNutritions().forEach(nutrition -> {
                NutritionItemResponse nutritionItemResponse = new NutritionItemResponse();
                nutritionItemResponse.setValue(nutrition.getValue());
                nutritionItemResponse.setNutrition(nutritionMap.getOrDefault(nutrition.getNutritionId(), null));
                nutritionItemResponses.add(nutritionItemResponse);
            });
            recipeResponse.setNutritions(nutritionItemResponses);

            responses.add(recipeResponse);
        });

        Page<RecipeResponse> responsePage =
                new PageImpl<>(responses, pageable, recipes.getTotalElements());

        return responsePage;
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

    // ===== TỔNG SỐ NGUYÊN LIỆU =====
    public Long totalRecipes() {
        return recipeRepository.count();
    }

    public Recipe getById(String id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
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