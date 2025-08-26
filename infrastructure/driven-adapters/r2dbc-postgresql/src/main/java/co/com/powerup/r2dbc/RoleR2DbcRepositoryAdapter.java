package co.com.powerup.r2dbc;

import co.com.powerup.model.role.Role;
import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup.r2dbc.helper.RoleEntity;
import co.com.powerup.r2dbc.helper.UserEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RoleR2DbcRepositoryAdapter extends ReactiveAdapterOperations<Role, RoleEntity, Long, RoleR2dbcRepository>
        implements RoleRepository {

    public RoleR2DbcRepositoryAdapter(RoleR2dbcRepository repository, ObjectMapper mapper) {
        super(repository, mapper, roleEntity -> mapper.map(roleEntity, Role.class));

    }

    @Override
    public Mono<Role> findById(long id) {
        return repository.findById(id)
                .map(this::toEntity);
    }

    @Override
    public Mono<Role> findByName(String name) {
        return repository.findByName(name)
                .map(this::toEntity);
    }
}
