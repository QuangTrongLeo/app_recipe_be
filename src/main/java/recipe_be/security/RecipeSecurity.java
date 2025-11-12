package recipe_be.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class RecipeSecurity {
    @Value("${api.recipe.app.url}")
    private String apiRecipeAppUrl;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChainRecipe(HttpSecurity http) throws Exception {
        String endpoint = "recipes";
        String fullEndpoint = apiRecipeAppUrl + "/" + endpoint + "/**";
        http
                .securityMatcher(fullEndpoint)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, fullEndpoint).permitAll()
                        .requestMatchers(HttpMethod.POST, fullEndpoint).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, fullEndpoint).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, fullEndpoint).hasAnyRole("ADMIN", "USER")

                        // Các request khác nếu có → yêu cầu xác thực
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
