package co.com.powerup.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta para creación exitosa.")
public class UserResponseDto {

    @Schema(hidden = true)
    private Long id;
    @Schema(description = "Nombres del usuario", example = "Juanito")
    private String name;
    @Schema(description = "Apellidos del usuario", example = "Pérez")
    private String lastname;
    @Schema(description = "Email del usuario", example = "juanito@example.com")
    private String email;

}
