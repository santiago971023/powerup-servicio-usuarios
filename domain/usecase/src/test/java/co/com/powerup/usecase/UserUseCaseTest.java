package co.com.powerup.usecase;

import co.com.powerup.model.exceptions.RoleNotFoundException;
import co.com.powerup.model.exceptions.UserAlreadyExistsException;
import co.com.powerup.model.exceptions.UserNotFoundException;
import co.com.powerup.model.role.Role;
import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.PasswordEncoderServicePort;
import co.com.powerup.model.user.gateways.UserRepository;
import org.junit.jupiter.api.DisplayName;
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

    private static final String DEFAULT_ROLE_NAME = "SOLICITANTE";

    @Test
    @DisplayName("Debería guardar un usuario existosamente.")
    void shouldSaveAnUserSuccessfully() {

        // GIVEN
        User userToSave = User.builder().email("user@test.com").password("plainPassword").build();
        Role defaultRole = Role.builder().id(1L).name(DEFAULT_ROLE_NAME).build();
        User savedUser = User.builder().id(1L).email("user@test.com").password("encodedPassword").role(defaultRole).build();

        when(userRepository.existsByEmail(userToSave.getEmail())).thenReturn(Mono.just(false));
        when(roleRepository.findByName(DEFAULT_ROLE_NAME)).thenReturn(Mono.just(defaultRole));
        when(passwordEncoderServicePort.encode(userToSave.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(savedUser));

        // WHEN
        Mono<User> result = userUseCase.saveUser(userToSave);

        // THEN
        StepVerifier.create(result)
                .expectNextMatches( user -> user.getId() != null
                        && user.getEmail().equals(userToSave.getEmail())
                        && user.getPassword().equals("encodedPassword")
                        && user.getRole().getName().equals(DEFAULT_ROLE_NAME))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando no encuentre el rol.")
    void shouldThrowExceptionWhenRoleNotFound() {

        // GIVEN
        User userToSave = User.builder().email("user@test.com").password("plainPassword").build();
        when(userRepository.existsByEmail(userToSave.getEmail())).thenReturn(Mono.just(false));
        when(roleRepository.findByName(DEFAULT_ROLE_NAME)).thenReturn(Mono.empty());

        // WHEN
        Mono<User> result = userUseCase.saveUser(userToSave);

        // THEN
        StepVerifier.create(result)
                .expectError(RoleNotFoundException.class)
                .verify();

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoderServicePort, never()).encode(any(String.class));

    }

    @Test
    @DisplayName("Debería lanzar una excepción cuando el email ya esté registrado.")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // GIVEN
        User savedUser = User.builder().id(1L).email("test@test.com").password("encodedPassword").build(); // usuario ya existente

        when(userRepository.existsByEmail(any(String.class))).thenReturn(Mono.just(true));

        // WHEN
        Mono<User> result = userUseCase.saveUser(savedUser);

        // THEN
        StepVerifier.create(result)
                .expectError(UserAlreadyExistsException.class)
                .verify();

    }

    @Test
    @DisplayName("Debería obtener un usuario existosamente por su documento.")
    void shouldGetUserByIdCardSuccessfully() {
        // GIVEN
        String testIdCard = "123456789";
        User expectedUser = User.builder().id(1L).idCard(testIdCard).email("test@test.com").build();
        when(userRepository.findByIdCard(testIdCard)).thenReturn(Mono.just(expectedUser));

        // WHEN
        Mono<User> result = userUseCase.getUserByIdCard(testIdCard);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(user -> user.getIdCard().equals(testIdCard))
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería retornar una excepción cuando el documento no exista.")
    void shouldThrowExceptionWhenIdCardDoesExist(){
        //GIVEN
        String nonExistentIdCard = "9999999999";
        when(userRepository.findByIdCard(nonExistentIdCard)).thenReturn(Mono.empty());

        // WHEN
        Mono<User> result = userUseCase.getUserByIdCard(nonExistentIdCard);

        // THEN
        StepVerifier.create(result)
                .expectError(UserNotFoundException.class)
                .verify();
    }

}
