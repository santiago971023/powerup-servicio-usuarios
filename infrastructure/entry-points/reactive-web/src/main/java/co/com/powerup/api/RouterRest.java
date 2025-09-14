package co.com.powerup.api;


import co.com.powerup.api.dto.ErrorResponseDto;
import co.com.powerup.api.dto.UserRequestDto;
import co.com.powerup.api.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperation(
            path = "/api/v1/users",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.POST,
            beanClass = UserHandler.class,
            beanMethod = "saveUser",
            operation = @Operation(
                    operationId = "saveUser",
                    summary = "Registrar un nuevo usuario",
                    description = "Crea un nuevo usuario solicitante en el sistema.",
                    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            required = true,
                            description = "Datos del nuevo usuario a registrar",
                            content = @Content(schema = @Schema(implementation = UserRequestDto.class))
                    ),
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    description = "Usuario creado exitosamente",
                                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))
                            ),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Error de validación en los datos de entrada",
                                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
                            ),
                            @ApiResponse(
                                    responseCode = "409",
                                    description = "Conflicto, el usuario ya existe",
                                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
                            )
                    }
            )
    )
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return route(
                POST("/api/v1/users").and(accept(MediaType.APPLICATION_JSON)),
                userHandler::saveUser
        )
                .andRoute(
                        GET("/api/v1/users/document/{idCard}"),
                        userHandler::getUserByIdCard
                )
                .andRoute(
                    POST("/api/v1/login").and(accept(MediaType.APPLICATION_JSON)),
                    userHandler::loginUser

        );
    }

}

