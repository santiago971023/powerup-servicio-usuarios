package co.com.powerup.r2dbc;

import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup.r2dbc.helper.UserEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
public class UserR2DbcRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity, Long, UserR2dbcRepository>
        implements UserRepository {

    private final RoleRepository roleRepository;

    public UserR2DbcRepositoryAdapter(UserR2dbcRepository repository, ObjectMapper mapper, RoleRepository roleRepository) {
        super(repository, mapper, userEntity -> mapper.map(userEntity, User.class));

        this.roleRepository = roleRepository;
    }

    @Override
    protected UserEntity toData(User user){
        UserEntity userEntity = super.toData(user);
        if(user.getRole() != null && user.getRole().getId() != null) {
            userEntity.setRoleId(user.getRole().getId());
        }
        return userEntity;
    }

    @Override
    @Transactional
    public Mono<User> save(User user) {
        return super.save(user);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email) // Devuelve Mono<UserEntity>
                .flatMap(this::mapEntityWithRole);
    }

    @Override
    public Mono<User> findById(Long id) {
        return repository.findById(id) // Devuelve Mono<UserEntity>
                .flatMap(this::mapEntityWithRole);
    }

    @Override
    public Mono<User> findByIdCard(String idCard) {
        return repository.findByIdCard(idCard).map(userEntity ->
                mapper.map(userEntity, User.class));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<User> update(User user) {
        return this.save(user);
    }


    // privados
    private Mono<User> mapEntityWithRole(UserEntity userEntity) {
        User user = this.toEntity(userEntity);

        if(userEntity.getRoleId() != null) {
            return roleRepository.findById(userEntity.getRoleId())
                    .map(role -> {
                        user.setRole(role);
                        return user;
                    })
                    .defaultIfEmpty(user);
        }
        return Mono.just(user);
    }
}
