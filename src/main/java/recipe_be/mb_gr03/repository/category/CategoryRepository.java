package recipe_be.mb_gr03.repository.category;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import recipe_be.mb_gr03.entity.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
}
