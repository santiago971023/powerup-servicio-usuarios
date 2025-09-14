package co.com.powerup.model.token;

import co.com.powerup.model.user.User;

public interface TokenProvider {

    String generateToken(User user);

    boolean validateToken(String token);

    TokenClaims getAllClaimsFromToken(String token);

}
