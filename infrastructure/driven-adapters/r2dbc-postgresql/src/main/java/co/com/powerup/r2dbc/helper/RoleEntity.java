package co.com.powerup.r2dbc.helper;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@NoArgsConstructor
@Table("roles")
public class RoleEntity {

    @Id
    private Long id;

    private String name;
}
