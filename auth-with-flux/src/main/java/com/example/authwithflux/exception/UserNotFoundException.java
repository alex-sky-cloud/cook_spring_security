package com.example.authwithflux.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(final long id) {
        super("User not found for id=" + id);
    }
}
