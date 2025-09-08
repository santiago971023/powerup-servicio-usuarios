package co.com.powerup.api;

import co.com.powerup.api.dto.UserRequestDto;
import co.com.powerup.api.dto.UserResponseDto;
import co.com.powerup.api.mapper.IUserDtoMapper;
import co.com.powerup.model.exceptions.UserAlreadyExistsException;
import co.com.powerup.model.user.User;
import co.com.powerup.usecase.UserUseCase;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Component
@Slf4j
public class UserHandler {

    private final UserUseCase userUseCase;
    private final IUserDtoMapper userDtoMapper;
    private final Validator validator;

    public UserHandler(UserUseCase userUseCase, IUserDtoMapper userDtoMapper, Validator validator) {
        this.userUseCase = userUseCase;
        this.userDtoMapper = userDtoMapper;
        this.validator = validator;
    }


    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {

        log.info("== == Inicio de petición recibida para guarduar un usuario (solicitante). == ==");

        return serverRequest.bodyToMono(UserRequestDto.class)
                .doOnNext(dto -> log.debug("Dto extraído del cuerpo de la petición {}", dto))
                .flatMap(this::validateRequestDto)
                .map(userDtoMapper::toDomain)
                .flatMap(userUseCase::saveUser)
                .flatMap(savedUser -> {
                    UserResponseDto responseDto = userDtoMapper.toResponseDto(savedUser);
                    log.info("<== FIN: Usuario creado con éxito con ID: {}", savedUser.getId());
                    return ServerResponse.status(HttpStatus.CREATED).bodyValue(responseDto);
                });
    }

    public Mono<ServerResponse> getUserByIdCard(ServerRequest serverRequest) {
        String idCard = serverRequest.pathVariable("idCard");
        log.info("==> Petición recibida para buscar usuario por idCard: {}", idCard);

        return userUseCase.getUserByIdCard(idCard)
                .flatMap(user -> {
                    UserResponseDto responseDto = userDtoMapper.toResponseDto(user);
                    log.info("<== FIN: Usuario encontrado. Devolviendo respuesta 200 OK.");
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(responseDto);

                });
    }


    // Metodo privados  CLASE APARTE
    private Mono<UserRequestDto> validateRequestDto(UserRequestDto dto) {
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(dto);
        if(violations.isEmpty()) {
            return Mono.just(dto);
        }
        log.warn("! VALIDACIÓN FALLIDA: Se encontraron {} violaciones en el DTO: {}", violations.size(), violations);
        return Mono.error(new ConstraintViolationException(violations));
    }

    private Mono<ServerResponse> handleValidationException(ConstraintViolationException e ){
        var errorMessages = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        log.warn(" Fin: Petición rechazada por error de validación. Errores: {}", errorMessages);
        return ServerResponse.badRequest().bodyValue(errorMessages);
    }

    private Mono<ServerResponse> handleBusinessException(UserAlreadyExistsException e) {
        log.warn("Fin: Petición rechazada por regla de negocio: {}", e.getMessage());
        return ServerResponse.status(HttpStatus.CONFLICT).bodyValue(e.getMessage());
    }
}
