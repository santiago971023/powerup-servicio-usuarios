package co.com.powerup.r2dbc.helper;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Table(name = "usuarios")
public class UserEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    @Column(name = "password")
    private String password;

    @Column(name = "salario_base")
    private BigDecimal salarioBase;

    @Column(name = "role_id")
    private Long roleId;
}


