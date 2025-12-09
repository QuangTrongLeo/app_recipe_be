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
    
    private final String API_PREFIX ="/recipe-app/api/v1";
    
    private final String[] PUBLIC_URLS = {
            API_PREFIX + "/auth/login",
            API_PREFIX + "/auth/register"
    };

    private final String[] ADMIN_RESOURCES = {
            API_PREFIX + "/categories",
    };

    @Value("${app.jwt.secret}")
    private String signerKey;

    @Autowired
    private CustomJwtDecoder jwtDecoder;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(request ->{
            request
                    .requestMatchers(HttpMethod.POST, PUBLIC_URLS).permitAll()
                    .requestMatchers(HttpMethod.POST, ADMIN_RESOURCES).hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, ADMIN_RESOURCES).hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, ADMIN_RESOURCES).hasAuthority("ADMIN")
                    .anyRequest().authenticated();
        });
        httpSecurity.logout(logout ->{
            logout.disable();
        });

        httpSecurity.oauth2ResourceServer(oauth2 ->{
            oauth2.jwt(jwtConfigurer  -> jwtConfigurer.decoder( jwtDecoder)
                    .jwtAuthenticationConverter(jwtAuthenticationConverter()
                    ));
        });
        return httpSecurity.build();
    }
    
    @Bean
    public JwtDecoder jwtDecoder() {

        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder
                .withSecretKey( secretKeySpec )
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
    
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return converter;
    }
}
