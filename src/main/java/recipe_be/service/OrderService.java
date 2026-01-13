package recipe_be.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recipe_be.dto.request.OrderRequest;
import recipe_be.entity.*;
import recipe_be.enums.ErrorCode;
import recipe_be.enums.OrderStatus;
import recipe_be.enums.PaymentStatus;
import recipe_be.exception.AppException;
import recipe_be.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CartRepository cartRepository;
    private final IngredientRepository ingredientRepository;
    
    public Order findById(String id) {
        return orderRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public Order create(OrderRequest request) {
        if (request.getUserId() == null || request.getRecipeId() == null) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }
        
        User user = userService.getUserById(request.getUserId());
        Cart cart = cartRepository.findByUserId(request.getUserId()).orElseThrow(() -> 
                new AppException(ErrorCode.NOT_FOUND));

        double totalPrice = 0L;
        List<OrderItem> orderItems = new ArrayList<>();
        
        CartItem cartItem = cart.getItems().stream()
                .filter(t-> t.getRecipeId().equals(request.getRecipeId())).findFirst().orElse(null);

        assert cartItem != null;
        List<IngredientItem> ingredientItems = cartItem.getIngredients();
        
        List<String> ingredientIds = cartItem.getIngredients().stream()
                .map(IngredientItem::getIngredientId)
                .toList();

        Map <String, Ingredient> ingredientMap = ingredientRepository.findByIdIn(ingredientIds).stream()
                .collect(Collectors.toMap(
                        Ingredient::getId,
                        Function.identity()
                ));
        
        for (IngredientItem item : ingredientItems) {
            Ingredient ingredient = ingredientMap.get(item.getIngredientId());
            if (ingredient == null) {
                throw new AppException(ErrorCode.NOT_FOUND);
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setIngredient(ingredient);
            orderItem.setQuantity(item.getQuantity());
            orderItems.add(orderItem);
            totalPrice += ingredient.getPrice() * item.getQuantity();
            ingredient.setStock( ingredient.getStock() - item.getQuantity());
        }
        
        // TODO: Update stock
//        List<Ingredient> ingredients = ingredientMap.values().stream()
//                .filter(Objects::nonNull)
//                .toList();
//        ingredientRepository.saveAll(ingredients);

        cart.getItems().removeIf(t-> t.getRecipeId().equals(request.getRecipeId()));
        cartRepository.save(cart);
        
        Order order = new Order();
        order.setUserId(user.getId());
        order.setStatus(OrderStatus.SUCCESS);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);
        order.setPaymentStatus(PaymentStatus.WAITING);
        orderRepository.save(order);
        return order;
    }
    
    public List<Order> getAllByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order getById(String id) {
        return orderRepository.findById(id).orElseThrow( () -> new AppException(ErrorCode.NOT_FOUND));
    }

    public Boolean updateStatus(String id, OrderStatus orderStatus) {
        try {
            Order order = orderRepository.findById(id).orElseThrow( () -> new AppException(ErrorCode.NOT_FOUND));
            order.setStatus(orderStatus);
            orderRepository.save(order);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }



}
