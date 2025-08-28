package co.com.powerup.api.config;

import co.com.powerup.api.dto.ErrorDetailDto;
import co.com.powerup.api.dto.ErrorResponseDto;
import co.com.powerup.model.exceptions.BusinessException;
import co.com.powerup.model.exceptions.UserAlreadyExistsException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Order(-2)
@Slf4j
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {


    /**
     * Create a new {@code AbstractErrorWebExceptionHandler}.
     *
     * @param errorAttributes    the error attributes
     * @param resources          the resources configuration properties
     * @param applicationContext the application context
     * @since 2.4.0
     */
    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties webProperties,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer configurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);
        HttpStatus httpStatus;
        List<ErrorDetailDto> errorDetails = new ArrayList<>();
        String message;
        String errorCode;

        if (error instanceof ConstraintViolationException ex) {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = "Error de validación en los datos de entrada.";
            errorCode = "400_01";
            errorDetails = ex.getConstraintViolations().stream()
                    .map(violation -> new ErrorDetailDto(
                            ((PathImpl) violation.getPropertyPath()).getLeafNode().getName(),
                            violation.getMessage()
                    ))
                    .collect(Collectors.toList());
            log.warn("Error de validación {}", errorDetails);

        } else if (error instanceof BusinessException) {
            if (error instanceof UserAlreadyExistsException) {
                httpStatus = HttpStatus.CONFLICT;
                message = "El usuario ya está creado.";
                errorCode = "409_01";
                errorDetails.add(new ErrorDetailDto("user", error.getMessage()));
            } else { // Para alguna otra business exception que voy a implementar
                httpStatus = HttpStatus.BAD_REQUEST;
                message = "Petición inválida.";
                errorCode = "400_02";
                errorDetails.add(new ErrorDetailDto("businessRule", error.getMessage()));
            }
            log.warn("Error de negocio {}", error.getMessage());
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Error en el servidor.";
            errorCode = "500_01";
            errorDetails.add(new ErrorDetailDto("error", "Error interno."));
            log.error("Error no controlado.", error);
        }

        ErrorResponseDto finalResponse = ErrorResponseDto.builder()
                .errors(errorDetails)
                .message(message)
                .code(errorCode)
                .build();

        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(finalResponse));
    }
}
