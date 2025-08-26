package co.com.powerup.r2dbc.helper;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "roles")
public class RoleEntity {

    @Id
    private Long id;

    private String name;
}
