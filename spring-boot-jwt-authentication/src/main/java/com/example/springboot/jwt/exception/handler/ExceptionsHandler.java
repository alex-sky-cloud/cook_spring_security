package com.example.springboot.jwt.exception.handler;

import com.example.springboot.jwt.exception.bean.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {

        log.error(ex.getClass().getCanonicalName());


        final ErrorResponse apiError = ErrorResponse.builder()
                .message(ex.getLocalizedMessage())
                .status(HttpStatus.UNAUTHORIZED)
                .build();

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {

        log.error(ex.getClass().getCanonicalName());


        final ErrorResponse apiError = ErrorResponse.builder()
                .message(ex.getLocalizedMessage())
                .errorTimeOccurs(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED)
                .build();

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
