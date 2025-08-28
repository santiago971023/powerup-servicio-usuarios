package co.com.powerup.api.dto;

import lombok.Builder;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Data
@Builder
public class ErrorResponseDto {

    private List<ErrorDetailDto> errors;
    private String message;
    private String code;

}
