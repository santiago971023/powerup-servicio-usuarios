package co.com.powerup.model.usuario.gateways;

import co.com.powerup.model.usuario.Usuario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {

    public Mono<Usuario> findByEmail(String email);
    public Mono<Usuario> findById(Long id);
    public Mono<Usuario> findByCedula(String cedula);
    public Mono<Usuario> save(Usuario usuario);
    public Mono<Void> delete(Long id);
    public Flux<Usuario> findAll();
    public Mono<Usuario> update(Usuario usuario);

}
