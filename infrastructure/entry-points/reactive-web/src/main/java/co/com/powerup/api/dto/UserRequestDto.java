package co.com.powerup.api.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {


    private String idCard;
    @NotBlank(message = "El campo 'name' no puede ser nulo o vacío.")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s\\p{Punct}]+$", message = "El campo 'name' solo debe contener letras y espacios.")
    @Size(min = 3, message = "El campo 'name' debe tener más de tres letras.")
    private String name;

    @NotBlank(message = "El campo 'lastname' no puede ser nulo o vacío.")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s\\p{Punct}]+$", message = "El campo 'lastname' solo debe contener letras y espacios.")
    @Size(min = 3, message = "El campo 'lastname' debe tener más de tres letras.")
    private String lastname;

    @NotNull(message = "El campo 'birthday' no puede ser nulo.")
    private LocalDate birthday;

    private String address;
    private String phone;

    @NotBlank(message = "El campo 'email' no puede ser nulo o vacío.")
    @Email(message = "El formato del 'email' no es válido.")
    private String email;

    @NotBlank(message = "La contraseña no puede ser nula o vacía.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;

    @NotNull(message = "El salario base no puede ser nulo.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El salario base debe ser mayor que cero.")
    @DecimalMax(value = "15000000.0", message = "El salario base no puede exceder los 15,000,000.")
    private BigDecimal salary;

}
