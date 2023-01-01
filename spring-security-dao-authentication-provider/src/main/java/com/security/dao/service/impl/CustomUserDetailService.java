package com.security.dao.service.impl;

import com.security.dao.model.UserEntity;
import com.security.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        final UserEntity customer = userRepository.findByEmail(email);

        if (customer == null) {
            throw new UsernameNotFoundException(email);
        }
        return User
                .withUsername(customer.getEmail())
                .password(customer.getPassword())
                .authorities("USER") /*здесь искусственно назначаем найденному пользователю, уровень доступа - USER*/
                .build();
    }
}
