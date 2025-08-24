package co.com.powerup.r2dbc;

import co.com.powerup.model.usuario.Usuario;
import co.com.powerup.r2dbc.helper.ReactiveAdapterOperations;
import co.com.powerup.r2dbc.helper.UsuarioEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioR2dbcRepositoryAdapter extends ReactiveAdapterOperations<Usuario,
        UsuarioEntity,
        Long,
        UsuarioR2dbcRepository> {
    public UsuarioR2dbcRepositoryAdapter(UsuarioR2dbcRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Object.class/* change for domain model */));
    }

}
