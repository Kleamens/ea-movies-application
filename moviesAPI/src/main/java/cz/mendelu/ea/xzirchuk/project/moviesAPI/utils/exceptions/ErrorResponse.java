package cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private final String message;
    private final CodeEnum errorCode;

}
