package recipe_be.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import recipe_be.dto.response.NutritionResponse;
import recipe_be.entity.Nutrition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface NutritionRepository extends MongoRepository<Nutrition, String> {
    List<Nutrition> findByIdIn(Collection<String> ids);
}
