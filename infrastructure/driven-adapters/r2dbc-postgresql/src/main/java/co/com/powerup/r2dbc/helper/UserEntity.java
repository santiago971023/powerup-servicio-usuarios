package co.com.powerup.r2dbc.helper;

import co.com.powerup.model.role.Role;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_card", nullable = false, unique = true)
    private String idCard;

    @Column(nullable = false)
    private String name;

    @Column( nullable = false)
    private String lastname;

    private LocalDate birthday;

    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(precision = 15, scale = 2)
    private BigDecimal salary;

    @Column(name = "role_id") // Mapea a la columna de la clave foránea en tu tabla 'users'
    private Long roleId;
}


