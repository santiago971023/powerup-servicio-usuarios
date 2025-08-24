package co.com.powerup.usecase.usuario;

import co.com.powerup.model.exceptions.UserAlreadyExistsException;
import co.com.powerup.model.exceptions.UserNotFoundException;
import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserGateway;
import co.com.powerup.model.user.gateways.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;


public class UserUseCase implements UserGateway {

    private final static Logger LOGGER = Logger.getLogger(UserUseCase.class.getName());

    private final UserRepository userRepository; // Ya está inyectado por constructor

    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Mono<User> saveUser(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(userExisting ->  {
                    LOGGER.warning( "El usuario con el email '"+ user.getEmail() + "' ya existe.");
                    return Mono.error(new UserAlreadyExistsException("El email " + user.getEmail() + " ya está registrado."));
                })
                .switchIfEmpty(Mono.defer( () -> {
                    LOGGER.info( "Guardando nuevo usuario con email: " + user.getEmail() );
                    return userRepository.save(user);
                }))
                .cast(User.class);
    }

    @Override
    public Mono<User> getUserByIdCard(String idCard) {
        return userRepository.findByEmail(idCard);
    }

    @Override
    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> updateUser(User user) {
        return userRepository.findById(user.getId())
                .flatMap(userFound -> {
                    LOGGER.info("Usuario encontrado, se procede a actualizar.");
                    return userRepository.save(user);
                })
                .switchIfEmpty(Mono.defer( () -> {
                    LOGGER.warning("Intento de actualización fallido porque no se encuentra usuario con el id: " + user.getId());
                    return Mono.error(new UserNotFoundException("No se puede actualizar. Usuario con ID " + user.getId() + " no fue encontrado."));
                }));
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        return userRepository.findById(id)
                .flatMap(userFound -> {
                    LOGGER.info("Usuario encontrado, se procede a eliminar.");
                    return userRepository.delete(id);
                })
                .switchIfEmpty(Mono.defer( () -> {
                    LOGGER.warning("Está intentando eiminar un usuario no existente.");
                    return Mono.error(new UserNotFoundException("No se puede eliminar. Usuario con ID: " + id + " no fue encontrado."));
                }));
    }
}
