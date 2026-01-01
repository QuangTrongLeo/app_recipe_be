package recipe_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import recipe_be.dto.request.cart.CartItemRequest;
import recipe_be.dto.response.IngredientItemResponse;
import recipe_be.dto.response.IngredientResponse;
import recipe_be.dto.response.RecipeResponse;
import recipe_be.dto.response.cart.CartItemResponse;
import recipe_be.dto.response.cart.CartResponse;
import recipe_be.entity.*;
import recipe_be.mapper.CartMapper;
import recipe_be.mapper.IngredientMapper;
import recipe_be.mapper.RecipeMapper;
import recipe_be.repository.CartRepository;
import recipe_be.repository.IngredientRepository;
import recipe_be.repository.RecipeRepository;
import recipe_be.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;
    
    public void create(Cart cart) {
        cartRepository.save(cart);
    }
    
    public CartResponse getCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found."));

        Cart cart = cartRepository.findByUserId(user.getId()).orElse(null);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(user.getId());
            cartRepository.save(cart);
        }
        
        List<String> recipeIds = new ArrayList<>();
        cart.getItems().forEach(item -> {
            recipeIds.add(item.getRecipeId());
        });
        
        List<RecipeResponse> recipes = recipeMapper.toRecipeResponseList(recipeRepository.findByIdIn(recipeIds));
        Map<String, RecipeResponse> recipeMap = recipes.stream().collect(Collectors.toMap(RecipeResponse::getId, recipe -> recipe));

        List<CartItemResponse> responses = cart.getItems().stream()
                .filter(i -> recipeMap.containsKey(i.getRecipeId()))
                .map(i -> {
                    RecipeResponse r = recipeMap.get(i.getRecipeId());
                    r.setIngredients(null);
                    r.setNutritions(null);
                    return CartItemResponse.builder().recipe(r).build();
                })
                .toList();

        CartResponse cartResponse = CartResponse.builder()
                .items(responses)
                .build();
        
        return cartResponse;
    }
    
    public CartResponse addToCart( String userId, CartItemRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> 
                new RuntimeException("User not found."));
        Cart cart = cartRepository.findByUserId(user.getId()).orElse(null);
        Recipe recipe = recipeRepository.findById(request.getRecipeId()).orElseThrow(()-> new RuntimeException("Recipe not found."));

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(user.getId());
            cartRepository.save(cart);
        }
        
        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }
        
        for (CartItem item : cart.getItems()) {
            if (item.getRecipeId().equals(request.getRecipeId())) {
               throw new RuntimeException("Recipe already exist.");
            }
        }
        
        CartItem cartItem = CartItem.builder()
                .recipeId(request.getRecipeId())
                .ingredients(recipe.getIngredients())
                .recipe(recipe)
                .build();
        
        cart.getItems().add(cartItem);
        cartRepository.save(cart);
        
        return cartMapper.toResponse(cart);
    }
    
    public boolean removeItem(String userId, CartItemRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found."));
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> 
                new RuntimeException("Cart not found."));

        boolean removed = cart.getItems().removeIf(
                item -> item.getRecipeId().equals(request.getRecipeId())
        );
        if (!removed) throw new RuntimeException("Item not found in cart.");
        cartRepository.save(cart);
        
        return removed ;
    }
    
    public CartItemResponse getItemDetail(String userId, String recipeId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found."));
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() ->
                new RuntimeException("Cart not found."));
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new RuntimeException("Recipe not found."));

        CartItem cartItem = cart.getItems().stream()
                .filter(i -> i.getRecipeId().equals(recipeId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        List<String> ingredientIds = new ArrayList<>();

        cartItem.getIngredients().forEach(ingredient -> {
            ingredientIds.add(ingredient.getIngredientId());
        });

        List<IngredientResponse> ingredientResponse = ingredientMapper.toIngredientRponseList(ingredientRepository.findByIdIn(ingredientIds));
        Map<String, IngredientResponse> ingredientMap = ingredientResponse.stream().collect(Collectors.toMap(IngredientResponse::getId, ingredient -> ingredient));
        
        CartItemResponse cartItemResponse = new CartItemResponse();
        List<IngredientItemResponse> ingredientResponseList = new ArrayList<>();
        
        cartItemResponse.setRecipe(recipeMapper.toShortRecipeResponse(recipe));
        cartItem.getIngredients().forEach(ingredient -> {
            
            IngredientItemResponse ingredientItemResponse = new IngredientItemResponse();
            if (ingredientMap.containsKey(ingredient.getIngredientId())) {
                ingredientItemResponse.setIngredient(ingredientMap.get(ingredient.getIngredientId()));
            }
            ingredientItemResponse.setQuantity(ingredient.getQuantity());
            ingredientResponseList.add(ingredientItemResponse);
        });
        
        cartItemResponse.setItems(ingredientResponseList);
        return cartItemResponse;
        
    }
    
    
    public CartResponse updateQuantity( String userId, CartItemRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> 
                new RuntimeException("User not found."));
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() ->
            new RuntimeException("Cart not found."));
        
        List<String> ingredientIds = request.getIngredients().stream().map(IngredientItem::getIngredientId).toList();
        
        int countSize = ingredientRepository.countByIdIn(ingredientIds);
        if (countSize != ingredientIds.size()) {
            throw new RuntimeException("Invalid request.");
        }
        
        if (request.getRecipeId() == null || request.getRecipeId().equals("") || request.getIngredients().isEmpty()) {
            throw new RuntimeException("Recipe or Ingredient not found.");
        }

        CartItem cartItem = cart.getItems().stream().filter(i -> i.getRecipeId().equals(request.getRecipeId())).findFirst().orElse(null);
        if (cartItem == null) {
            throw new RuntimeException("Item not found.");
        }
        cartItem.setIngredients(request.getIngredients());
        cartRepository.save(cart);
        return cartMapper.toResponse(cart);
    }
    
    
}
