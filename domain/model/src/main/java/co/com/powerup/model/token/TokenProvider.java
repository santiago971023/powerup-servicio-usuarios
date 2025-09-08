package co.com.powerup.model.token;

import co.com.powerup.model.user.User;
import reactor.core.publisher.Mono;

public interface TokenProvider {

    String generateToken(User user);

}
