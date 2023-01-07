package com.reg.exception;

/**
 Исключение, создаваемое системой, если кто-то пытается зарегистрироваться
 с уже существующим в системе, идентификатором электронной почты.
 */
public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException() {
        super();
    }


    public UserAlreadyExistException(String message) {
        super(message);
    }


    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
