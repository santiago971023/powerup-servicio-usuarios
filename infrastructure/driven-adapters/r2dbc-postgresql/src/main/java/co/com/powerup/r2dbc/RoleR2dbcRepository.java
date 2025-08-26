package co.com.powerup.r2dbc;

import co.com.powerup.model.role.Role;
import co.com.powerup.r2dbc.helper.RoleEntity;
import co.com.powerup.r2dbc.helper.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface RoleR2dbcRepository extends ReactiveCrudRepository<RoleEntity, Long>, ReactiveQueryByExampleExecutor<RoleEntity> {

    Mono<RoleEntity> findByName(String name);

}
