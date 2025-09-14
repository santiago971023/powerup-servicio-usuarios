package co.com.powerup.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotBlank
    @Email
    private String username; //email

    @NotBlank
    private String password;
}
