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
                .flatMap(userRequestDto -> {
                    log.debug("Mapeando UserRequestDto a objet de dominio.");
                    User user = userDtoMapper.toDomain(userRequestDto);
                    log.info("Llamando a UserUseCase para guardar el usuario con el email.", user.getEmail());
                    return userUseCase.saveUser(user);
                })
                .flatMap(savedUser -> {
                    log.info("UserUseCase completado, usuario guardado");
                    UserResponseDto responseDto = userDtoMapper.toResponseDto(savedUser);
                    log.debug("Mapeando User a UserDResponseDto.");
                    return ServerResponse.status(HttpStatus.CREATED)
                            .bodyValue(responseDto);
                })
                .doOnSuccess(response -> log.info(" == == Fin. Petición procesada, se devuelve respuesta 201. "))
                .onErrorResume(ConstraintViolationException.class, this::handleValidationException)
                .onErrorResume(UserAlreadyExistsException.class, this::handleBusinessException);
    }


    // Metodo privados
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
