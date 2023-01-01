package com.security.dao.service.impl;

import com.security.dao.dto.UserDto;
import com.security.dao.exception.UserAlreadyExistException;
import com.security.dao.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Override
    @Transactional
    public void register(UserDto user) throws UserAlreadyExistException {

    }

}
