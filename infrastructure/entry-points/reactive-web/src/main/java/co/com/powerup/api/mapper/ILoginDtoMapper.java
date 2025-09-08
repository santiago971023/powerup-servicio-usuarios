package co.com.powerup.api.mapper;


import co.com.powerup.api.dto.LoginRequestDto;
import co.com.powerup.usecase.LoginUseCase;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ILoginDtoMapper {

    LoginUseCase.Input toInput(LoginRequestDto loginRequestDto);

}
