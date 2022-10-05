package com.example.authwithflux.exception.handlers;


import com.example.authwithflux.exception.UserAlreadyExistsException;
import com.example.authwithflux.exception.UserNotFoundException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private static final String STATUS_ATTRIBUTE = "status";

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = new LinkedHashMap<>();
        Throwable error = super.getError(request);

        HttpStatus errorStatus;
        int valueErrorStatus;
        Class<? extends Throwable> aClass = error.getClass();

        boolean isUserAlreadyExistsException = aClass.equals(UserAlreadyExistsException.class);
        boolean isUserNotFoundException = aClass.equals(UserNotFoundException.class);

        if (isUserAlreadyExistsException) {
            errorStatus = HttpStatus.NOT_ACCEPTABLE;
        } else if (isUserNotFoundException){
            errorStatus = HttpStatus.NOT_FOUND;
        } else {
            errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String errorMessage = error.getMessage();
        map.put("message", errorMessage);
        valueErrorStatus = errorStatus.value();
        map.put(STATUS_ATTRIBUTE, valueErrorStatus);

        return map;
    }
}

