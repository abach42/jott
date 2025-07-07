package com.abach42.jott.config;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDenied(AccessDeniedException exception,
            ServletWebRequest request) {
        return new ResponseEntity<>(
                new ErrorDto(getStatusCodeNumber(HttpStatus.FORBIDDEN),
                        getError(HttpStatus.FORBIDDEN),
                        exception.getMessage(), getPath(request)),
                HttpStatus.FORBIDDEN);
    }

    private int getStatusCodeNumber(HttpStatusCode httpStatus) {
        return httpStatus.value();
    }

    private String getError(HttpStatus httpStatus) {
        return httpStatus.getReasonPhrase();
    }

    private String getPath(ServletWebRequest request) {
        return request.getRequest().getRequestURI();
    }
}