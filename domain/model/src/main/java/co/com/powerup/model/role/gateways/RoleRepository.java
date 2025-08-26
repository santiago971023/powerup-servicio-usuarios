package co.com.powerup.model.role.gateways;

import co.com.powerup.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> findById(long id);
    Mono<Role> findByName(String name);
}
