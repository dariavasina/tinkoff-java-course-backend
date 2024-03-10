package edu.java.bot.controller;

import edu.java.model.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BadRequestControllerAdvice {
    private ApiErrorResponse buildResponse(Exception e, String description) {
        return ApiErrorResponse.builder()
            .description(description)
            .code(HttpStatus.BAD_REQUEST.toString())
            .exceptionName(e.getClass().getName())
            .exceptionMessage(e.getMessage())
            .stacktrace(Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toList())
            .build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> violations = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            violations.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(buildResponse(e, getDescription(violations)), HttpStatus.BAD_REQUEST);
    }



    private String getDescription(Map<String, String> violations) {
        StringBuilder description = new StringBuilder("constraint violation\n");
        for (var entry: violations.entrySet()) {
            description.append(entry.getKey());
            description.append(" - ");
            description.append(entry.getValue());
            description.append('\n');
        }
        return description.toString();
    }
}
