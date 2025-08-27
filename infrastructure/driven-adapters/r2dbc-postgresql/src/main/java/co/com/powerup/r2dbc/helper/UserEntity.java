package co.com.powerup.r2dbc.helper;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.math.BigDecimal;
import java.time.LocalDate;


@Table("users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserEntity {

    @Id
    private Long id;

    @Column("id_card")
    private String idCard;

    private String name;

    private String lastname;

    private LocalDate birthday;

    private String address;

    private String phone;

    private String email;

    private String password;

    private BigDecimal salary;

    @Column("role_id")
    private Long roleId;
}


