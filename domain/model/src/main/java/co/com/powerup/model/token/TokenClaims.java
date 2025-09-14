package co.com.powerup.model.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenClaims {

    private Long userId;
    private String email;
    private String role;

}
