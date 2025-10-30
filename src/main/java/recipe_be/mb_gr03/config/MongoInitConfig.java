package recipe_be.mb_gr03.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import recipe_be.mb_gr03.entity.*;
import recipe_be.mb_gr03.enums.EnumRole;
import recipe_be.mb_gr03.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Configuration
public class MongoInitConfig {

    @Value("${account.admin.email}")
    private String emailAdmin;

    @Value("${account.admin.password}")
    private String passwordAdmin;

    @Value("${account.admin.username}")
    private String usernameAdmin;

    @Bean
    public ApplicationRunner initMongoCollections(MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
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
                // User mẫu (trống)
                mongoTemplate.insert(
                        User.builder()
                                .email("")
                                .password("")
                                .username("")
                                .avatar("")
                                .bio("")
                                .role(EnumRole.USER)
                                .createdAt(DateTimeUtils.nowVietnamTime())
                                .favoriteRecipes(List.of())
                                .build(),
                        "users"
                );

                // Admin mặc định
                mongoTemplate.insert(
                        User.builder()
                                .email(emailAdmin)
                                .password(passwordEncoder.encode(passwordAdmin))
                                .username(usernameAdmin)
                                .avatar("data:image/jpeg;base64,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")
                                .bio("I'm ADMIN of recipe app")
                                .role(EnumRole.ADMIN)
                                .createdAt(DateTimeUtils.nowVietnamTime())
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
                                .createdAt(DateTimeUtils.nowVietnamTime())
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
                                .issuedAt(DateTimeUtils.nowVietnamTime())
                                .expiresAt(DateTimeUtils.nowVietnamTime())
                                .isRevoked(false)
                                .build(),
                        "refresh_tokens"
                );
            }
        };
    }
}
