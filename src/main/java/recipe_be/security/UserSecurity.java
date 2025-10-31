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
        http
                .securityMatcher(apiRecipeAppUrl + "/" + endpoint + "/**") // match /users/**
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // GET, PUT, POST cho USER hoặc ADMIN
                        .requestMatchers(HttpMethod.GET, apiRecipeAppUrl + "/" + endpoint + "/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, apiRecipeAppUrl + "/" + endpoint + "/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, apiRecipeAppUrl + "/" + endpoint + "/**").hasAnyRole("USER", "ADMIN")
                        // DELETE chỉ ADMIN
                        .requestMatchers(HttpMethod.DELETE, apiRecipeAppUrl + "/" + endpoint + "/**").hasRole("ADMIN")
                        // các request khác yêu cầu authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
