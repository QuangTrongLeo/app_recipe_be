package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.cart.CartItemRequest;
import recipe_be.dto.response.ApiResponse;
import recipe_be.dto.response.cart.CartItemResponse;
import recipe_be.dto.response.cart.CartResponse;
import recipe_be.service.CartService;

@RestController
@RequestMapping("/users/{id}/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @PreAuthorize("hasAnyAuthority('USER')")
    @PutMapping("/add-to-cart")
    public ApiResponse<CartResponse> addToCart(@RequestBody CartItemRequest request, @PathVariable("id") String userId){
        return ApiResponse.<CartResponse>builder()
                .result(cartService.addToCart(userId,request))
                .build();
    }
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("")
    public ApiResponse<CartResponse> getCart(@PathVariable("id") String userId){
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getCart(userId))
                .build();
    }
    
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/item-detail")
    public ApiResponse<CartItemResponse> getCartItems(@PathVariable("id") String userId, @RequestParam("recipeId") String recipeId){
        return ApiResponse.<CartItemResponse>builder()
                .result(cartService.getItemDetail(userId, recipeId))
                .build();
        
    }



    @PreAuthorize("hasAnyAuthority('USER')")
    @DeleteMapping("/remove-item")
    public ApiResponse<Boolean> removeItem(@RequestBody CartItemRequest request, @PathVariable("id") String userId){
        return ApiResponse.<Boolean>builder()
                .result(cartService.removeItem(userId,request))
                .build();
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PutMapping("/update")
    public ApiResponse<CartResponse> updateQuantity(@RequestBody CartItemRequest request, @PathVariable("id") String userId){
        return ApiResponse.<CartResponse>builder()
                .result(cartService.updateQuantity(userId,request))
                .build();
    }



}
