package co.com.powerup.usecase;

import co.com.powerup.model.exceptions.InvalidCredentialsException;
import co.com.powerup.model.token.TokenProvider;
import co.com.powerup.model.user.gateways.PasswordEncoderServicePort;
import co.com.powerup.model.user.gateways.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

public class LoginUseCase {

    private final static Logger LOGGER = Logger.getLogger(LoginUseCase.class.getName());

    private UserRepository userRepository;
    private PasswordEncoderServicePort passwordEncoder;
    private TokenProvider tokenProvider;

    public LoginUseCase(UserRepository userRepository, PasswordEncoderServicePort passwordEncoder, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Data
    @AllArgsConstructor
    public static class Input {
        private String username; //email
        private String password;
    }

    public Mono<String> login(Input input) {
        return userRepository.findByEmail(input.username)
                .switchIfEmpty(Mono.error(new InvalidCredentialsException("Credenciales inválidas.")))
                .flatMap(user -> {
                    LOGGER.info("Usuario encontrado, verificar contraseña");
                    if(passwordEncoder.matches(input.password, user.getPassword())) {
                        LOGGER.info("Contraseña correcta.");
                        String token = tokenProvider.generateToken(user);
                        return Mono.just(token);
                    } else {
                        LOGGER.info("Contraseña incorrecta.");
                        return Mono.error(new InvalidCredentialsException("Credenciales Inválidas."));
                    }
                });
    }

}
