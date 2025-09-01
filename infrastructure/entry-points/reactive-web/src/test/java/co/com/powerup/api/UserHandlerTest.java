package co.com.powerup.api;

import co.com.powerup.api.dto.UserRequestDto;
import co.com.powerup.api.dto.UserResponseDto;
import co.com.powerup.api.mapper.IUserDtoMapper;
import co.com.powerup.model.user.User;
import co.com.powerup.usecase.UserUseCase;
import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private IUserDtoMapper userDtoMapper;

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private Validator validator;

    @InjectMocks
    private UserHandler userHandler;

    @Test
    void saveUser() {

        ServerRequest serverRequest = mock(ServerRequest.class);
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .idCard("123456789")
                .name("John Doe")
                .lastname("Smith")
                .address("Address mocked")
                .phone("123456789")
                .birthday(LocalDate.parse("1960-12-25"))
                .email("john@doe.com")
                .salary(BigDecimal.valueOf(1500000))
                .password("password")
                .build();

        User user = User.builder()
                .id(1L)
                .idCard("123456789")
                .name("John Doe")
                .lastname("Smith")
                .address("Address mocked")
                .phone("123456789")
                .birthday(LocalDate.parse("1960-12-25"))
                .email("john@doe.com")
                .salary(BigDecimal.valueOf(1500000))
                .password("password")
                .build();

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();


        when(serverRequest.bodyToMono(UserRequestDto.class)).thenReturn(Mono.just(userRequestDto));
        when(userDtoMapper.toDomain(userRequestDto)).thenReturn(user);
        when(userUseCase.saveUser(user)).thenReturn(Mono.just(user));
        when(userDtoMapper.toResponseDto(user)).thenReturn(userResponseDto);
        when(validator.validate(any())).thenReturn(Collections.emptySet());


        Mono<ServerResponse> response = userHandler.saveUser(serverRequest);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> {
                    boolean statusOk = serverResponse.statusCode().equals(HttpStatus.CREATED);
                    return statusOk;
                })
                .verifyComplete();


    }
}