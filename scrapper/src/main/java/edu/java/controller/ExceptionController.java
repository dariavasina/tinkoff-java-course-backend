package edu.java.controller;

import edu.java.exception.AlreadyExistsException;
import edu.java.exception.NotExistException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = {AlreadyExistsException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "user already registered")
    public ErrorMessage chatAlreadyExistsException(AlreadyExistsException ex, WebRequest request) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {NotExistException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "does not exist")
    public ErrorMessage chatDoesNotExistException(NotExistException ex, WebRequest request) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "invalid args")
    public ErrorMessage illegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new ErrorMessage(ex.getMessage());
    }
}
