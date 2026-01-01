package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.cart.CartItemRequest;
import recipe_be.dto.response.APIResponse;
import recipe_be.service.CartService;

@RestController
@RequestMapping("/users/{id}/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("")
    public APIResponse getCart(@PathVariable("id") String userId){
        return APIResponse.builder(cartService.getCart(userId)).build();
    }
    
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/item-detail")
    public APIResponse getCartItems(@PathVariable("id") String userId, @RequestParam("recipeId") String recipeId){
        return APIResponse.builder(cartService.getItemDetail(userId, recipeId)).build();
        
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PutMapping("/add-to-cart")
    public APIResponse addToCart(@RequestBody CartItemRequest request, @PathVariable("id") String userId){
        return APIResponse.builder(cartService.addToCart(userId,request)).build();
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @DeleteMapping("/remove-item")
    public APIResponse removeItem(@RequestBody CartItemRequest request, @PathVariable("id") String userId){
        return APIResponse.builder(cartService.removeItem(userId,request)).build();
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PutMapping("/update")
    public APIResponse updateQuantity(@RequestBody CartItemRequest request, @PathVariable("id") String userId){
        return APIResponse.builder(cartService.updateQuantity(userId,request)).build();
    }



}
