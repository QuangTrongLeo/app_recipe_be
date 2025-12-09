package recipe_be.config;

import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import recipe_be.service.auth.AuthService;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${app.jwt.secret}")
    private String signerKey;

    @Autowired
    private AuthService authService;


    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            if (authService.verifyToken(token) == null) {
                throw new RuntimeException("Authentication failed");
            }
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }

        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HmacSHA256");
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();

        Jwt jwt = nimbusJwtDecoder.decode(token);
        return jwt;
    }
}
