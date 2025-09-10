package co.com.powerup.api.config;

import co.com.powerup.api.dto.ErrorDetailDto;
import co.com.powerup.api.dto.ErrorResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Rutas públicas
                        .pathMatchers("/api/v1/login").permitAll()

                        // Rutas protegidas por rol
                        .pathMatchers(HttpMethod.POST, "/api/v1/users").hasAnyAuthority("ADMINISTRADOR")

                        // Cualquier otra ruta, authentication.
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler()))
                .build();
    }

    @Bean
    public ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return (exchange, ex) -> {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED); // 401
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

            ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                    .errors(List.of(new ErrorDetailDto("authentication", "Se requiere un token válido para acceder a este recurso.")))
                    .message("Fallo de autenticación")
                    .code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                    .build();

            try {
                byte[] responseBytes = new ObjectMapper().writeValueAsBytes(errorResponse);
                DataBuffer buffer = response.bufferFactory().wrap(responseBytes);
                return response.writeWith(Mono.just(buffer));
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }
        };
    }

    @Bean
    public ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, ex) -> {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FORBIDDEN); // 403
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

            ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                    .errors(List.of(new ErrorDetailDto("permissions", "No tienes los permisos necesarios para acceder a este recurso.")))
                    .message("Fallo de autenticación")
                    .code(String.valueOf(HttpStatus.FORBIDDEN.value()))
                    .build();

            try {
                byte[] responseBytes = new ObjectMapper().writeValueAsBytes(errorResponse);
                DataBuffer buffer = response.bufferFactory().wrap(responseBytes);
                return response.writeWith(Mono.just(buffer));
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }
        };
    }

}
