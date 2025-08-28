package co.com.powerup.r2dbc;

import co.com.powerup.model.user.gateways.PasswordEncoderServicePort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


public class PasswordEncoderAdapter implements PasswordEncoderServicePort {

    private final PasswordEncoder encoder;

    public PasswordEncoderAdapter(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
