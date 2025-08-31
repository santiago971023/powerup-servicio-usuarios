package co.com.powerup.usecase;

import co.com.powerup.model.exceptions.UserAlreadyExistsException;
import co.com.powerup.model.role.Role;
import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.PasswordEncoderServicePort;
import co.com.powerup.model.user.gateways.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoderServicePort passwordEncoderServicePort;

    @InjectMocks
    private UserUseCase userUseCase;

    @Test
    void shouldSaveAnUserSuccessfully() {


        User userWithoutRole = User.builder().email("user@test.com").password("plainPassword").build();
        Role roleSolicitante = Role.builder().id(1L).name("SOLICITANTE").build();
        User savedUser = User.builder().id(1L).email("test@test.com").password("encodedPassword").build();


        when(userRepository.findByEmail(any(String.class))).thenReturn(Mono.empty());
        when(roleRepository.findByName("SOLICITANTE")).thenReturn(Mono.just(roleSolicitante));
        when(passwordEncoderServicePort.encode(userWithoutRole.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(savedUser));

        Mono<User> result = userUseCase.saveUser(userWithoutRole);


        StepVerifier.create(result)
                .expectNextMatches( user -> {
                    return user.getId() != null && user.getEmail().equals(savedUser.getEmail());
                })
                .verifyComplete();
        verify(passwordEncoderServicePort).encode("plainPassword");
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // GIVEN
        User savedUser = User.builder().id(1L).email("test@test.com").password("encodedPassword").build(); // usuario ya existente

        when(userRepository.findByEmail(any(String.class))).thenReturn(Mono.just(savedUser));

        // WHEN
        Mono<User> result = userUseCase.saveUser(savedUser);

        // THEN
        StepVerifier.create(result)
                .expectError(UserAlreadyExistsException.class)
                .verify();

    }

}
