package recipe_be.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import recipe_be.entity.Ingredient;

import java.util.Collection;
import java.util.List;

@Repository
public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    List<Ingredient> findByIdIn(Collection<String> ids);

    int countByIdIn(Collection<String> ids);
}
