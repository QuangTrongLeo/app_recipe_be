package recipe_be.mb_gr03.service.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import recipe_be.mb_gr03.entity.User;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.access-expiration}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${app.jwt.refresh-expiration}")
    private long REFRESH_TOKEN_EXPIRATION;

    // === Tạo Access Token ===
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .addClaims(Map.of("role", user.getRole().name()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // === Tạo Refresh Token ===
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // === Lấy username từ token ===
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // === Kiểm tra token hợp lệ ===
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // === Kiểm tra token hết hạn ===
    public boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // === Giải mã token ===
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT token đã hết hạn", e);
        } catch (JwtException e) {
            throw new RuntimeException("JWT không hợp lệ", e);
        }
    }

    // === Sinh key ký JWT từ secret ===
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
    }
}
