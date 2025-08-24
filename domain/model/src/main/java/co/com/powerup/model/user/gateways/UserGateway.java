package co.com.powerup.model.user.gateways;

import co.com.powerup.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserGateway {

    Mono<User> saveUser(User user);
    Mono<User> getUserByIdCard(String idCard);
    Mono<User> getUserById(Long id);
    Mono<User> getUserByEmail(String email);
    Flux<User> getAllUsers();
    Mono<User> updateUser(User user);
    Mono<Void> deleteUser(Long id);

}
