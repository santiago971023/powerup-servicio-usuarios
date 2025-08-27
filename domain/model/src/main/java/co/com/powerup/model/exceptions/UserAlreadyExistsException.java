package co.com.powerup.model.exceptions;

public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
