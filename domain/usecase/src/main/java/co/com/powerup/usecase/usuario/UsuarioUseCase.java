package co.com.powerup.usecase.usuario;

import co.com.powerup.model.exceptions.UsuarioAlreadyExistsException;
import co.com.powerup.model.exceptions.UsuarioNotFoundException;
import co.com.powerup.model.usuario.Usuario;
import co.com.powerup.model.usuario.gateways.UsuarioGateway;
import co.com.powerup.model.usuario.gateways.UsuarioRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;


public class UsuarioUseCase implements UsuarioGateway {

    private final static Logger LOGGER = Logger.getLogger(UsuarioUseCase.class.getName());

    private final UsuarioRepository usuarioRepository; // Ya está inyectado por constructor

    public UsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public Mono<Usuario> guardarUsuario(Usuario usuario) {
        return usuarioRepository.findByEmail(usuario.getEmail())
                .flatMap(usuarioExistente ->  {
                    LOGGER.warning( "El usuario con el email '"+ usuario.getEmail() + "' ya existe.");
                    return Mono.error(new UsuarioAlreadyExistsException("El email " + usuario.getEmail() + " ya está registrado."));
                })
                .switchIfEmpty(Mono.defer( () -> {
                    LOGGER.info( "Guardando nuevo usuario con email: " + usuario.getEmail() );
                    return usuarioRepository.save(usuario);
                }))
                .cast(Usuario.class);
    }

    @Override
    public Mono<Usuario> getUsuarioByCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula);
    }

    @Override
    public Mono<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Mono<Usuario> getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Flux<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Mono<Usuario> actualizarUsuario(Usuario usuario) {
        return usuarioRepository.findById(usuario.getId())
                .flatMap(usuarioEncontrado -> {
                    LOGGER.info("Usuario encontrado, se procede a actualizar.");
                    return usuarioRepository.save(usuario);
                })
                .switchIfEmpty(Mono.defer( () -> {
                    LOGGER.warning("Intento de actualización fallido porque no se encuentra usuario con el id: " + usuario.getId());
                    return Mono.error(new UsuarioNotFoundException("No se puede actualizar. Usuario con ID " + usuario.getId() + " no fue encontrado."));
                }));
    }

    @Override
    public Mono<Void> eliminarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .flatMap(usuarioEncontrado -> {
                    LOGGER.info("Usuario encontrado, se procede a eliminar.");
                    return usuarioRepository.delete(id);
                })
                .switchIfEmpty(Mono.defer( () -> {
                    LOGGER.warning("Está intentando eiminar un usuario no existente.");
                    return Mono.error(new UsuarioNotFoundException("No se puede eliminar. Usuario con ID: " + id + " no fue encontrado."));
                }));
    }
}
