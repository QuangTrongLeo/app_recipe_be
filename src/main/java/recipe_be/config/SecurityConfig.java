package recipe_be.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${api.recipe.app.url}")
    private String API_PREFIX;

    @Value("${app.jwt.secret}")
    private String signerKey;

    @Autowired
    private CustomJwtDecoder jwtDecoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String AUTH = API_PREFIX + "/auth";
        String CATEGORIES = API_PREFIX + "/categories";
        String INGREDIENTS = API_PREFIX + "/ingredients";
        String NUTRITION = API_PREFIX + "/nutrition";
        String RECIPES = API_PREFIX + "/recipes";
        String USERS = API_PREFIX + "/users";

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth
                // 1. AUTH
                .requestMatchers(HttpMethod.POST, AUTH + "/**").permitAll()

                // 2. CATEGORIES (user: GET, admin: CRUD)
                .requestMatchers(HttpMethod.GET, CATEGORIES + "/**").hasAnyAuthority("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, CATEGORIES + "/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, CATEGORIES + "/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, CATEGORIES + "/**").hasAuthority("ADMIN")

                // 3. INGREDIENTS (user: GET, admin: CRUD)
                .requestMatchers(HttpMethod.GET, INGREDIENTS + "/**").hasAnyAuthority("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, INGREDIENTS + "/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, INGREDIENTS + "/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, INGREDIENTS + "/**").hasAuthority("ADMIN")

                // 4. NUTRITION (user: GET, admin: CRUD)
                .requestMatchers(HttpMethod.GET, NUTRITION + "/**").hasAnyAuthority("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, NUTRITION + "/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, NUTRITION + "/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, NUTRITION + "/**").hasAuthority("ADMIN")

                // 5. RECIPES (user và admin: CRUD)
                .requestMatchers(HttpMethod.GET, RECIPES + "/**").hasAnyAuthority("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, RECIPES + "/**").hasAnyAuthority("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, RECIPES + "/**").hasAnyAuthority("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, RECIPES + "/**").hasAnyAuthority("USER", "ADMIN")

                // 6. USERS (user: GET PUT, admin: CRUD)
                .requestMatchers(HttpMethod.GET, USERS + "/**").hasAnyAuthority("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, USERS + "/**").hasAnyAuthority("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, USERS + "/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, USERS + "/**").hasAuthority("ADMIN")

                // 7. Mặc định: phải login
                .anyRequest().authenticated()
        );

        http.logout(l -> l.disable());

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                jwt.decoder(jwtDecoder).jwtAuthenticationConverter(jwtAuthenticationConverter())
        ));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(signerKey.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter granted = new JwtGrantedAuthoritiesConverter();
        granted.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(granted);
        return converter;
    }
}

