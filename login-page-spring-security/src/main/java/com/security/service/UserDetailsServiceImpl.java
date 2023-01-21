package com.security.service;


import com.security.model.UserModel;
import com.security.model.UserPrincipal;
import com.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        UserModel user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with email = " + userEmail + " not exist!"));

        return new UserPrincipal(user);
    }
}
