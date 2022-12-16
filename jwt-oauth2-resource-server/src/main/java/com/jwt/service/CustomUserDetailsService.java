package com.jwt.service;

import com.jwt.dtos.LoginRequestDto;
import com.jwt.dtos.LoginRequestRegistrationDto;
import com.jwt.model.User;
import com.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        User user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with email = " + userEmail + " not exist!"));

        return new UserPrincipal(user);
    }
}
