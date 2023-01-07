package com.reg.service;


import com.reg.dto.UserRegistrationDto;
import com.reg.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	User save(UserRegistrationDto registrationDto);

	List<User> getAll();
}
