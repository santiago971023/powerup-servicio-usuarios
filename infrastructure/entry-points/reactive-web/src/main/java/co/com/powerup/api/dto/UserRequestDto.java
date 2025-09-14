package co.com.powerup.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la creación de un nuevo usuario")
public class UserRequestDto {

    @Schema(description = "Identificación única del usuario (cédula o dni)", example = "1046461232")
    private String idCard;

    @Schema(description = "Nombres del usuario", example = "Juanito")
    @NotBlank(message = "El campo 'name' no puede ser nulo o vacío.")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s\\p{Punct}]+$", message = "El campo 'name' solo debe contener letras y espacios.")
    @Size(min = 3, message = "El campo 'name' debe tener más de tres letras.")
    private String name;

    @Schema(description = "Apellidos del usuario", example = "Pérez")
    @NotBlank(message = "El campo 'lastname' no puede ser nulo o vacío.")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s\\p{Punct}]+$", message = "El campo 'lastname' solo debe contener letras y espacios.")
    @Size(min = 3, message = "El campo 'lastname' debe tener más de tres letras.")
    private String lastname;

    @Schema(description = "Fecha de nacimiento (AAAA-mm-dd).", example = "1994-12-05")
    @NotNull(message = "El campo 'birthday' no puede ser nulo.")
    private LocalDate birthday;

    @Schema(description = "Dirección de residencia.", example = "Cra. 68 # 41 -25")
    private String address;

    @Schema(description = "Teléfono celular del usuario.", example = "+57 3016557787")
    private String phone;

    @Schema(description = "Email del usuario", example = "juani@example.com")
    @NotBlank(message = "El campo 'email' no puede ser nulo o vacío.")
    @Email(message = "El formato del 'email' no es válido.")
    private String email;

    @NotBlank(message = "La contraseña no puede ser nula o vacía.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;

    @Schema(description = "Salario del usuario", example = "1500000")
    @NotNull(message = "El salario base no puede ser nulo.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El salario base debe ser mayor que cero.")
    @DecimalMax(value = "15000000.0", message = "El salario base no puede exceder los 15,000,000.")
    private BigDecimal salary;


    @Schema(description = "El campo 'role' debe ser 'ASESOR', o por el contrario un valor nulo para 'SOLICITANTE'.", example = "'ASESOR'")
    private String role;


}
