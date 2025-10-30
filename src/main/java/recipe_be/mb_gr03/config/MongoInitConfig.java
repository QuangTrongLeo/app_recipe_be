package recipe_be.mb_gr03.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import recipe_be.mb_gr03.entity.*;
import recipe_be.mb_gr03.enums.EnumRole;

import java.util.Date;
import java.util.List;

@Configuration
public class MongoInitConfig {

    @Bean
    public ApplicationRunner initMongoCollections(MongoTemplate mongoTemplate) {
        return args -> {
            // Danh sách collections cần tạo
            String[] collections = {
                    "ingredients", "users", "orders", "recipes",
                    "payments", "refresh_tokens", "carts",
                    "images", "icons", "nutrition"
            };

            for (String col : collections) {
                if (!mongoTemplate.collectionExists(col)) {
                    mongoTemplate.createCollection(col);
                }
            }

            // === INSERT DOCUMENT MẪU (nếu chưa có) ===
            if (mongoTemplate.getCollection("users").countDocuments() == 0) {
                mongoTemplate.insert(
                        User.builder()
                                .email("")
                                .password("")
                                .username("")
                                .avatar("")
                                .bio("")
                                .role(EnumRole.USER)
                                .createdAt(new Date())
                                .favoriteRecipes(List.of())
                                .build(),
                        "users"
                );
            }

            if (mongoTemplate.getCollection("ingredients").countDocuments() == 0) {
                mongoTemplate.insert(
                        Ingredient.builder()
                                .name("")
                                .image("")
                                .unit("")
                                .unitPrice(0)
                                .stock(0)
                                .build(),
                        "ingredients"
                );
            }

            if (mongoTemplate.getCollection("recipes").countDocuments() == 0) {
                mongoTemplate.insert(
                        Recipe.builder()
                                .name("")
                                .description("")
                                .image("")
                                .instructions("")
                                .time(0)
                                .ingredients(List.of())
                                .nutritions(List.of())
                                .build(),
                        "recipes"
                );
            }

            if (mongoTemplate.getCollection("orders").countDocuments() == 0) {
                mongoTemplate.insert(
                        Order.builder()
                                .userId("")
                                .totalPrice(0)
                                .createdAt(new Date())
                                .items(List.of())
                                .build(),
                        "orders"
                );
            }

            if (mongoTemplate.getCollection("payments").countDocuments() == 0) {
                mongoTemplate.insert(
                        Payment.builder()
                                .orderId("")
                                .userId("")
                                .amount(0)
                                .transactionId("")
                                .build(),
                        "payments"
                );
            }

            if (mongoTemplate.getCollection("carts").countDocuments() == 0) {
                mongoTemplate.insert(
                        Cart.builder()
                                .userId("")
                                .totalPrice(0)
                                .isCheckedOut(false)
                                .items(List.of())
                                .build(),
                        "carts"
                );
            }

            if (mongoTemplate.getCollection("images").countDocuments() == 0) {
                mongoTemplate.insert(new Image(null, ""), "images");
            }

            if (mongoTemplate.getCollection("icons").countDocuments() == 0) {
                mongoTemplate.insert(new Icon(null, ""), "icons");
            }

            if (mongoTemplate.getCollection("nutrition").countDocuments() == 0) {
                mongoTemplate.insert(
                        Nutrition.builder()
                                .name("")
                                .icon("")
                                .unit("")
                                .type("")
                                .build(),
                        "nutrition"
                );
            }

            if (mongoTemplate.getCollection("refresh_tokens").countDocuments() == 0) {
                mongoTemplate.insert(
                        RefreshToken.builder()
                                .userId("")
                                .refreshToken("")
                                .issuedAt(new Date())
                                .expiresAt(new Date())
                                .isRevoked(false)
                                .build(),
                        "refresh_tokens"
                );
            }
        };
    }
}
