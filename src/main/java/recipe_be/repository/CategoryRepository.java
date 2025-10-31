package recipe_be.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import recipe_be.entity.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
}
