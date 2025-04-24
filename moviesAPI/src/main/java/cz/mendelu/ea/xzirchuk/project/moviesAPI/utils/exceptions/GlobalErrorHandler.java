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
    public ResponseEntity<ErrorResponse> exceptionBadInput(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),CodeEnum.BAD_INPUT);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);

    }
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> exceptionAlreadyExists(AlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),CodeEnum.ALREADY_EXISTS);
        return new ResponseEntity<>(errorResponse, CONFLICT);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> exceptionNotFound(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),CodeEnum.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }
}
