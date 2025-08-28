package co.com.powerup.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetailDto {

    private String field;
    private String description;

}
