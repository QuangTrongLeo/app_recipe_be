package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.request.OrderRequest;
import recipe_be.dto.response.ApiResponse;
import recipe_be.entity.Order;
import recipe_be.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    
    @PostMapping(value = "")
    public ApiResponse<Order> addOrder(@RequestBody OrderRequest request){
        return ApiResponse.<Order>builder()
                .result(orderService.create(request))
                .build();
    }
    
    @GetMapping(value = "")
    public ApiResponse<List<Order>> get(@Param("userId") String userId){
        return ApiResponse.<List<Order>>builder()
                .result(orderService.getAllByUserId(userId))
                .build();
    }

    @GetMapping(value = "/{id}")
    public ApiResponse<Order> getById(@PathVariable(value = "id") String id){
        return ApiResponse.<Order>builder()
                .result(orderService.getById(id))
                .build();
    }
    
    @PutMapping(value = "/{id}")
    public ApiResponse<Boolean> updateStatus(@PathVariable(value = "id") String id, @RequestBody OrderRequest orderRequest){
        return ApiResponse.<Boolean>builder()
                .result(orderService.updateStatus(id,orderRequest.getOrderStatus()))
                .build();
    }

}
