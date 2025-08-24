package co.com.powerup.api.mapper;

import co.com.powerup.api.dto.UserRequestDto;
import co.com.powerup.api.dto.UserResponseDto;
import co.com.powerup.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserDtoMapper {

    User toDomain(UserRequestDto requestDto);
    UserResponseDto toResponseDto(User user);

}
