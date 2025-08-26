package co.com.powerup.r2dbc.mapper;

import co.com.powerup.model.user.User;
import co.com.powerup.r2dbc.helper.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {

    @Mapping(source = "role.id", target = "roleId") // Le digo que tome el id de Role y lo ponga en el campo roleId
    UserEntity toEntity(User user);

    @Mapping(target = "role", ignore = true)
    User toDomain(UserEntity userEntity);

}
