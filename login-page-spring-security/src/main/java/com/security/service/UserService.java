package com.security.service;

import com.security.dto.UserDataDto;
import com.security.exception.UserAlreadyExistException;

public interface UserService {

    void register(UserDataDto user) throws UserAlreadyExistException;

    boolean checkIfUserExist(String email);
}
