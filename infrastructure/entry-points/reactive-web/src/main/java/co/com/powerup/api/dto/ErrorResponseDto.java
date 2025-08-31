package co.com.powerup.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Data
@Builder
@Schema(description = "Listado de errores individuales.")
public class ErrorResponseDto {

    @Schema(
            description = "Lista de errores detallados.",
            implementation = ErrorDetailDto.class
    )
    private List<ErrorDetailDto> errors;

    @Schema(
            description = "Mensaje general que describe el error.",
            example = "Petición inválida debido a una regla de negocio."
    )
    private String message;

    @Schema(
            description = "Código interno del error.",
            example = "400_99"
    )
    private String code;

}
