package cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BadInputException extends RuntimeException{
    public BadInputException(String message){
        super(message);
    }
}
