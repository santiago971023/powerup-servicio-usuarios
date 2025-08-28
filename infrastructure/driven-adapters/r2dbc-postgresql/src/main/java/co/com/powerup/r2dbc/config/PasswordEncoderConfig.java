package co.com.powerup.r2dbc.config;

import co.com.powerup.model.user.gateways.PasswordEncoderServicePort;
import co.com.powerup.r2dbc.PasswordEncoderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoderServicePort passwordEncoderServicePort() {
        return new PasswordEncoderAdapter(new BCryptPasswordEncoder());
    }


}
