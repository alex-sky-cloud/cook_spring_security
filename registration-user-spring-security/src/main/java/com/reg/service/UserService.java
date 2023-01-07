package com.reg.service;

import com.reg.dto.UserDataDto;
import com.reg.exception.UserAlreadyExistException;

public interface UserService {

    void register(UserDataDto user) throws UserAlreadyExistException;

    boolean checkIfUserExist(String email);
}
