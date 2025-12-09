package recipe_be.service.auth;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipe_be.dto.request.auth.LoginRequest;
import recipe_be.dto.request.auth.RegisterRequest;
import recipe_be.dto.response.auth.AuthenticationResponse;
import recipe_be.entity.User;
import recipe_be.service.UserService;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    @Value("${app.jwt.secret}")
    private String SIGNER_KEY;

    public boolean register(RegisterRequest request) {
        userService.createUser(request);
        return true;
    }
    
    public AuthenticationResponse login(LoginRequest LoginRequest) {
        User user = userService.getUserByEmail(LoginRequest.getEmail());
        if (user == null) {
            throw new RuntimeException("Authentication failed");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!passwordEncoder.matches(LoginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Authentication failed");
        }

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setUser(user);
        authenticationResponse.setToken(generateToken(user));

        return authenticationResponse;
    }
    
    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer(user.getUsername())
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("userId", user.getId())
                .expirationTime(new Date(
                        Instant.now().plus(36000, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);


        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);

        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        
        boolean valid = signedJWT.verify(verifier);

        if (!(valid && expirationDate.after(new Date())) ){
            return null;
        }
        return signedJWT;
    }
    private String buildScope(User user) {
        return user.getRole().name();
    }
}
