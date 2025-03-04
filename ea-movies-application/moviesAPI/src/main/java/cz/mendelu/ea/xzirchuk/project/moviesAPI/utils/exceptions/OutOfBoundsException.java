package cz.mendelu.ea.xzirchuk.project.moviesAPI.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OutOfBoundsException extends  RuntimeException{
}

