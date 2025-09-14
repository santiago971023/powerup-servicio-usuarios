package co.com.powerup.config;


import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.model.token.TokenProvider;
import co.com.powerup.model.user.gateways.PasswordEncoderServicePort;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.usecase.LoginUseCase;
import co.com.powerup.usecase.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = "co.com.powerup.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

        @Bean
        public LoginUseCase loginUseCase(UserRepository userRepository,
                                         PasswordEncoderServicePort passwordEncoder,
                                         TokenProvider tokenProvider) {
                return new LoginUseCase(userRepository, passwordEncoder, tokenProvider);
        }

        @Bean
        public UserUseCase userUseCase(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoderServicePort passwordEncoder) {
                return new UserUseCase(userRepository, roleRepository, passwordEncoder);
        }

}
