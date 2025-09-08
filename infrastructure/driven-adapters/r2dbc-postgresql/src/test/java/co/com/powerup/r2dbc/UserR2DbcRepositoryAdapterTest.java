package co.com.powerup.r2dbc;

import co.com.powerup.model.role.Role;
import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.model.user.User;
import co.com.powerup.r2dbc.helper.RoleEntity;
import co.com.powerup.r2dbc.helper.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserR2DbcRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    UserR2DbcRepositoryAdapter repositoryAdapter;

    @Mock
    UserR2dbcRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    RoleRepository roleRepository;


    @Test
    void mustMapUserToUserEntity() {
        // Arrange
        Role role = new Role(1L, "ROLE_USER");
        User user = User.builder()
                .id(1L)
                .email("test@example")
                .role(role)
                .build();
        // devolvemos un UserEntity válido cuando el mapper sea invocado
        when(mapper.map(any(User.class), eq(UserEntity.class)))
                .thenReturn(UserEntity.builder()
                        .id(1L)
                        .email("test@example")
                        .build()
                );
        // Act
        UserEntity userEntity = repositoryAdapter.toData(user);
        // Assert
        assertEquals(user.getId(), userEntity.getId());
        assertEquals(user.getEmail(), userEntity.getEmail());
        assertEquals(userEntity.getRoleId(), user.getRole().getId());
    }

    @Test
    void mustFindValueById() {
        // Arrange
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email("test@example")
                .roleId(1L)
                .build();

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        User user = User.builder()
                .id(1L)
                .email("test@example")
                .role(role)
                .build();

        when(repository.findById(eq(1L))).thenReturn(Mono.just(userEntity));
        when(roleRepository.findById(eq(1L))).thenReturn(Mono.just(role));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        // Act
        Mono<User> result = repositoryAdapter.findById(1L);
        // Asserts
        StepVerifier.create(result)
                .assertNext(user1 -> {
                    assertEquals(1L, user1.getId());
                    assertEquals("test@example", user1.getEmail());
                    assertEquals(1L, user1.getRole().getId());
                    assertEquals("ROLE_USER", user1.getRole().getName());
                    assertNotNull(user1.getRole());
                })
                .verifyComplete();

    }

    @Test
    void mustFindValueByEmail() {
        // Arrange
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email("test@example.com")
                .roleId(1L)
                .build();

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .role(role)
                .build();

        when(repository.findByEmail(eq("test@example.com"))).thenReturn(Mono.just(userEntity));
        when(roleRepository.findById(eq(1L))).thenReturn(Mono.just(role));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        // Act
        Mono<User> result = repositoryAdapter.findByEmail("test@example.com");
        // Asserts
        StepVerifier.create(result)
                .assertNext(user1 -> {
                    assertEquals(1L, user1.getId());
                    assertEquals("test@example.com", user1.getEmail());
                    assertEquals(1L, user1.getRole().getId());
                    assertEquals("ROLE_USER", user1.getRole().getName());
                    assertNotNull(user1.getRole());
                })
                .verifyComplete();

    }


}
