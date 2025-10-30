package recipe_be.mb_gr03.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import recipe_be.mb_gr03.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
