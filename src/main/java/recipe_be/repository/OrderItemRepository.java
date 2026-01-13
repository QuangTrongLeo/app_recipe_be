package recipe_be.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import recipe_be.entity.OrderItem;

@Repository
public interface OrderItemRepository extends MongoRepository<OrderItem, String> {
}
