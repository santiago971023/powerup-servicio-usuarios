package co.com.powerup.api;

import co.com.powerup.api.dto.UserRequestDto;
import co.com.powerup.api.dto.UserResponseDto;
import co.com.powerup.api.mapper.ILoginDtoMapper;
import co.com.powerup.api.mapper.IUserDtoMapper;
import co.com.powerup.model.user.User;
import co.com.powerup.usecase.LoginUseCase;
import co.com.powerup.usecase.UserUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;






@Disabled
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @MockitoBean
    private IUserDtoMapper userDtoMapper;

    @MockitoBean
    private ILoginDtoMapper loginDtoMapper;


    @Test
    @DisplayName("Debería guardar exitosamente un usuario y retornar 201 Created.")
    void shouldCreateUserSuccessfully() {

        // GIVEN
        User userToCreate = User.builder().email("test@test.com").password("plainPassword").build();
        User createdUser = User.builder().id(1L).email("test@test.com").password("encodedPassword").build();
        UserResponseDto createdUserResponseDto = UserResponseDto.builder().id(1L).email("test@test.com").build();

        when(userDtoMapper.toDomain(any(UserRequestDto.class))).thenReturn(User.builder().email("test@test.com").password("plainPassword").build());
        when(userDtoMapper.toResponseDto(any(User.class))).thenReturn(createdUserResponseDto);

        when(userUseCase.saveUser(any(User.class))).thenReturn(Mono.just(createdUser));

        // WHEN AND THEN
        webTestClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userToCreate)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.email").isEqualTo("test@test.com");

    }


    @Test
    void testListenGETUseCase() {
        webTestClient.get()
                .uri("/api/usecase/path")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isEmpty();
                        }
                );
    }




}
