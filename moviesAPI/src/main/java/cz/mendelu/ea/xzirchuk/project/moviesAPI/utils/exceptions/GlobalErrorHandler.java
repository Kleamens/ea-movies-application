package cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE;

@ControllerAdvice
public class GlobalErrorHandler {
//    For the needs of global error handling
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity exceptionValidationDirector(MethodArgumentNotValidException ex) {
    ErrorResponse errorResponse = new ErrorResponse("Body Content Invalid", BAD_REQUEST);
    return new ResponseEntity<>(errorResponse.getMessage(),errorResponse.getHttpStatus());
}
}
