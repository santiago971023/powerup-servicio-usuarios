package co.com.powerup.model.exceptions;

public enum ErrorMessageBusiness {

    USER_NOT_FOUND_EXCEPTION("No se ha encontrado un usuario con el id indicado."),
    USER_ALREADY_EXISTS_EXCEPTION("El usuario ya existe"),
    ROLE_NOT_FOUND_EXCEPTION("No se ha encontrado el rol por defecto.");


    private final String message;

    ErrorMessageBusiness(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
