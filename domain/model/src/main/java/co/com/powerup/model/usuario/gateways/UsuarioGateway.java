package co.com.powerup.model.usuario.gateways;

import co.com.powerup.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioGateway {

    Mono<Usuario> guardarUsuario(Usuario usuario);
    Mono<Usuario> getUsuarioByCedula(String cedula);
    Mono<Usuario> getUsuarioById(Long id);
    Mono<Usuario> getUsuarioByEmail(String email);
    Flux<Usuario> getAllUsuarios();
    Mono<Usuario> actualizarUsuario(Usuario usuario);
    Mono<Void> eliminarUsuario(Long id);

}
