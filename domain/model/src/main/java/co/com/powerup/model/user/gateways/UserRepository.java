package co.com.powerup.model.user.gateways;

import co.com.powerup.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    public Mono<User> findByEmail(String email);
    public Mono<User> findById(Long id);
    public Mono<User> findByIdCard(String idCard);
    public Mono<User> save(User user);
    public Mono<Void> delete(Long id);
    public Flux<User> findAll();
    public Mono<User> update(User user);

    public Mono<Boolean> existsByEmail(String email);

}
