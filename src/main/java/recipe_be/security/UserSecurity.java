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
public class UserSecurity {
    @Value("${api.recipe.app.url}")
    private String apiRecipeAppUrl;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChainUser(HttpSecurity http) throws Exception {
        String endpoint = "users";
        String fullEndpoint = apiRecipeAppUrl + "/" + endpoint + "/**";
        http
                .securityMatcher(fullEndpoint) // chỉ áp dụng cho /users/**
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Cho phép USER & ADMIN thực hiện GET, PUT, POST
                        .requestMatchers(HttpMethod.GET, fullEndpoint).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, fullEndpoint).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, fullEndpoint).hasAnyRole("USER", "ADMIN")
                        // Chỉ ADMIN được phép DELETE
                        .requestMatchers(HttpMethod.DELETE, fullEndpoint).hasRole("ADMIN")
                        // Các request khác yêu cầu xác thực
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
