package co.com.powerup.usecase;

import co.com.powerup.model.exceptions.ErrorMessageBusiness;
import co.com.powerup.model.exceptions.RoleNotFoundException;
import co.com.powerup.model.exceptions.UserAlreadyExistsException;
import co.com.powerup.model.exceptions.UserNotFoundException;
import co.com.powerup.model.role.gateways.RoleRepository;
import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.PasswordEncoderServicePort;
import co.com.powerup.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@RequiredArgsConstructor
public class UserUseCase {

    private final static Logger LOGGER = Logger.getLogger(UserUseCase.class.getName());

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderServicePort encoderPort;
    private static final String DEFAULT_ROLE_NAME = "SOLICITANTE";


    public Mono<User> saveUser(User user) {
        LOGGER.info("Empezando método saveUser del Caso de Uso.");

        String targetRole = (user.getRole() == null) ? DEFAULT_ROLE_NAME : user.getRole().getName();

        return userRepository.existsByEmail(user.getEmail())
                .flatMap(exists -> {
                    if(exists) {
                        LOGGER.warning("El usuario con el email '" + user.getEmail() + "' ya existe.");
                        return Mono.<User>error(new UserAlreadyExistsException(ErrorMessageBusiness.USER_ALREADY_EXISTS_EXCEPTION.getMessage()));
                    }
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.defer(() -> {
                    LOGGER.info("Guardando nuevo usuario con email: " + user.getEmail());
                    return roleRepository.findByName(targetRole)
                            .switchIfEmpty(Mono.error(new RoleNotFoundException(ErrorMessageBusiness.ROLE_NOT_FOUND_EXCEPTION.getMessage())))
                            .flatMap(role -> {
                                LOGGER.info("Encriptando contraseña.");
                                String encoderPassword = encoderPort.encode(user.getPassword());
                                LOGGER.info("Asignando contraseña encriptada.");
                                user.setPassword(encoderPassword);
                                LOGGER.info("Asignando rol: " + role.getName());
                                user.setRole(role);
                                return userRepository.save(user);
                            });
                }));
    }


    public Mono<User> getUserByIdCard(String idCard) {
        LOGGER.info("== == Se inicia método por llamado de microservice-loan == ==");
        return userRepository.findByIdCard(idCard)
                .switchIfEmpty(Mono.error(new UserNotFoundException(ErrorMessageBusiness.USER_NOT_FOUND_EXCEPTION.getMessage())));
    }

//
//    public Mono<User> getUserById(Long id) {
//        return userRepository.findById(id);
//    }
//
//
//    public Mono<User> getUserByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
//
//
//    public Flux<User> getAllUsers() {
//        return userRepository.findAll();
//    }


//    public Mono<User> updateUser(User user) {
//        return userRepository.findById(user.getId())
//                .flatMap(userFound -> {
//                    LOGGER.info("Usuario encontrado, se procede a actualizar.");
//                    return userRepository.save(user);
//                })
//                .switchIfEmpty(Mono.defer( () -> {
//                    LOGGER.warning("Intento de actualización fallido porque no se encuentra usuario con el id: " + user.getId());
//                    return Mono.error(new UserNotFoundException("No se puede actualizar. Usuario con ID " + user.getId() + " no fue encontrado."));
//                }));
//    }


//    public Mono<Void> deleteUser(Long id) {
//        return userRepository.findById(id)
//                .flatMap(userFound -> {
//                    LOGGER.info("Usuario encontrado, se procede a eliminar.");
//                    return userRepository.delete(id);
//                })
//                .switchIfEmpty(Mono.defer( () -> {
//                    LOGGER.warning("Está intentando eiminar un usuario no existente.");
//                    return Mono.error(new UserNotFoundException("No se puede eliminar. Usuario con ID: " + id + " no fue encontrado."));
//                }));
//    }
}
