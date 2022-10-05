package com.example.authwithflux.exception.handlers;


import com.example.authwithflux.domain.dto.ErrorDto;
import com.example.authwithflux.exception.UserAlreadyExistsException;
import com.example.authwithflux.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorDto> handleUserNotFoundException(Exception ex) {

        ErrorDto errorDto = new ErrorDto(ex.getMessage());

        return Mono.just(errorDto);
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Mono<ErrorDto> handleUserAlreadyExistsException(Exception ex) {

        ErrorDto errorDto = new ErrorDto(ex.getMessage());

        return Mono.just(errorDto);
    }
}
