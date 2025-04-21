package cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalErrorHandler {
//    For the needs of global error handling
    @ExceptionHandler({BadInputException.class,MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> BadInputExceptionDirector(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(),BAD_REQUEST);

    }
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity exceptionDirectorExists(AlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(),CONFLICT);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity exceptionDirectorNotFound(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(),NOT_FOUND);
    }
}
