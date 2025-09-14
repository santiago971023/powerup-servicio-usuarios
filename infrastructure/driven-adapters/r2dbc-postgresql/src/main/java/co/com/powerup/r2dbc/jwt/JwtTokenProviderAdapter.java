package co.com.powerup.r2dbc.jwt;

import co.com.powerup.model.token.TokenClaims;
import co.com.powerup.model.token.TokenProvider;
import co.com.powerup.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProviderAdapter implements TokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String generateToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().getName());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public TokenClaims getAllClaimsFromToken(String token) {
        Claims libraryClaims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return TokenClaims.builder()
                .userId(libraryClaims.get("userId", Long.class))
                .email(libraryClaims.getSubject())
                .role(libraryClaims.get("role", String.class))
                .build();
    }
}
