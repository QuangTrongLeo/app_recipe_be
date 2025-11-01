package recipe_be.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import recipe_be.entity.Nutrition;

@Repository
public interface NutritionRepository extends MongoRepository<Nutrition, String> {
}
