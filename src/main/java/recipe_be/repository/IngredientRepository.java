package recipe_be.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import recipe_be.entity.Ingredient;

@Repository
public interface IngredientRepository extends MongoRepository<Ingredient, String> {
}
