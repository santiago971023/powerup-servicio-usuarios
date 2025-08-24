package co.com.powerup.r2dbc;

import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup.r2dbc.helper.UserEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserR2DbcRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity, Long, UserR2dbcRepository>
        implements UserRepository {

    private final UserR2dbcRepository repository;
    private final ObjectMapper mapper;

    public UserR2DbcRepositoryAdapter(UserR2dbcRepository repository, ObjectMapper mapper) {
        super();
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return null;
    }

    @Override
    public Mono<User> findByIdCard(String idCard) {
        return null;
    }

    @Override
    public Mono<Void> delete(Long id) {
        return null;
    }

    @Override
    public Mono<User> update(User user) {
        return null;
    }
}
