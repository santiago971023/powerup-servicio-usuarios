package co.com.powerup.model.user.gateways;

// Este será mi puerto de salida para encriptar y validar contraseñas
public interface PasswordEncoderServicePort {

    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);

}
