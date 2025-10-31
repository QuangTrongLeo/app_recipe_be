package recipe_be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import recipe_be.entity.*;
import recipe_be.enums.Role;
import recipe_be.utils.DateTimeUtils;

import java.util.Arrays;
import java.util.List;

@Configuration
public class MongoInitConfig {

    @Value("${account.admin.email}")
    private String emailAdmin;

    @Value("${account.admin.password}")
    private String passwordAdmin;

    @Value("${account.admin.username}")
    private String usernameAdmin;

//    @Bean
//    public ApplicationRunner initMongoCollections(MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
//        return args -> {
//            // Danh sách collections cần tạo
//            String[] collections = {
//                    "ingredients", "users", "orders", "recipes",
//                    "payments", "refresh_tokens", "carts",
//                    "images", "icons", "nutrition", "categories"
//            };
//
//            for (String col : collections) {
//                if (!mongoTemplate.collectionExists(col)) {
//                    mongoTemplate.createCollection(col);
//                }
//            }
//
//            // === INSERT DOCUMENT MẪU (nếu chưa có) ===
//            if (mongoTemplate.getCollection("users").countDocuments() == 0) {
//                // User mẫu (trống)
//                mongoTemplate.insert(
//                        User.builder()
//                                .email("")
//                                .password("")
//                                .username("")
//                                .avatar("")
//                                .bio("")
//                                .role(Role.USER)
//                                .createdAt(DateTimeUtils.nowVietnamTime())
//                                .favoriteRecipes(List.of())
//                                .build(),
//                        "users"
//                );
//
//                // Admin mặc định
//                mongoTemplate.insert(
//                        User.builder()
//                                .email(emailAdmin)
//                                .password(passwordEncoder.encode(passwordAdmin))
//                                .username(usernameAdmin)
//                                .avatar("")
//                                .bio("I'm ADMIN of recipe app")
//                                .role(Role.ADMIN)
//                                .createdAt(DateTimeUtils.nowVietnamTime())
//                                .favoriteRecipes(List.of())
//                                .build(),
//                        "users"
//                );
//            }
//
//            if (mongoTemplate.getCollection("categories").countDocuments() == 0) {
//                mongoTemplate.insert(
//                        Arrays.stream(EnumCategory.values())
//                                .map(e -> Category.builder().name(e).build())
//                                .toList(),
//                        "categories"
//                );
//            }
//
//            if (mongoTemplate.getCollection("ingredients").countDocuments() == 0) {
//                mongoTemplate.insert(
//                        Ingredient.builder()
//                                .name("")
//                                .image("")
//                                .unit("")
//                                .unitPrice(0)
//                                .stock(0)
//                                .build(),
//                        "ingredients"
//                );
//            }
//
//            if (mongoTemplate.getCollection("recipes").countDocuments() == 0) {
//                mongoTemplate.insert(
//                        Recipe.builder()
//                                .name("")
//                                .description("")
//                                .image("")
//                                .instructions("")
//                                .time(0)
//                                .ingredients(List.of())
//                                .nutritions(List.of())
//                                .build(),
//                        "recipes"
//                );
//            }
//
//            if (mongoTemplate.getCollection("orders").countDocuments() == 0) {
//                mongoTemplate.insert(
//                        Order.builder()
//                                .userId("")
//                                .totalPrice(0)
//                                .createdAt(DateTimeUtils.nowVietnamTime())
//                                .items(List.of())
//                                .build(),
//                        "orders"
//                );
//            }
//
//            if (mongoTemplate.getCollection("payments").countDocuments() == 0) {
//                mongoTemplate.insert(
//                        Payment.builder()
//                                .orderId("")
//                                .userId("")
//                                .amount(0)
//                                .transactionId("")
//                                .build(),
//                        "payments"
//                );
//            }
//
//            if (mongoTemplate.getCollection("carts").countDocuments() == 0) {
//                mongoTemplate.insert(
//                        Cart.builder()
//                                .userId("")
//                                .totalPrice(0)
//                                .isCheckedOut(false)
//                                .items(List.of())
//                                .build(),
//                        "carts"
//                );
//            }
//
//            if (mongoTemplate.getCollection("images").countDocuments() == 0) {
//                mongoTemplate.insert(new Image(null, ""), "images");
//            }
//
//            if (mongoTemplate.getCollection("icons").countDocuments() == 0) {
//                mongoTemplate.insert(new Icon(null, ""), "icons");
//            }
//
//            if (mongoTemplate.getCollection("nutrition").countDocuments() == 0) {
//                mongoTemplate.insert(
//                        Nutrition.builder()
//                                .name("")
//                                .icon("")
//                                .unit("")
//                                .type("")
//                                .build(),
//                        "nutrition"
//                );
//            }
//
//            if (mongoTemplate.getCollection("refresh_tokens").countDocuments() == 0) {
//                mongoTemplate.insert(
//                        RefreshToken.builder()
//                                .userId("")
//                                .refreshToken("")
//                                .issuedAt(DateTimeUtils.nowVietnamTime())
//                                .expiresAt(DateTimeUtils.nowVietnamTime())
//                                .isRevoked(false)
//                                .build(),
//                        "refresh_tokens"
//                );
//            }
//        };
//    }
}
