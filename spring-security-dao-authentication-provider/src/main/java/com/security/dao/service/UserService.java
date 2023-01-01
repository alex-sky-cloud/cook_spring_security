package com.security.dao.service;


import com.security.dao.dto.UserDto;
import com.security.dao.exception.UserAlreadyExistException;
import com.security.dao.model.UserEntity;

public interface UserService {

    void register(final UserDto user) throws UserAlreadyExistException;
}
