package recipe_be.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import recipe_be.entity.Recipe;

import java.util.Collection;
import java.util.List;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findByUserId(String userId);
    List<Recipe> findByCategoryId(String categoryId);
//<<<<<<< HEAD
//
//    List<Recipe> findByIdIn(Collection<String> ids);
//=======
    List<Recipe> findByNameContainingIgnoreCase(String keyword);
//>>>>>>> cc420a5 (feat: impl search recipe by keyword)
}
