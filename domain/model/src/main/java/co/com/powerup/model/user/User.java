package co.com.powerup.model.user;
import co.com.powerup.model.role.Role;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String idCard;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String address;
    private String phone;
    private String email;
    private String password;
    private BigDecimal salary;
    private Role role;
}
