package com.example.service;

import com.example.model.CustomerModel;
import com.example.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final CustomerModel customer = customerRepository.findByEmail(username); /*email*/

        Set<UserRole> roles = new HashSet<>();
        roles.add(new UserRole("USER"));
        roles.add(new UserRole("ADMIN"));

        if (customer == null) {
            throw new UsernameNotFoundException(username);
        }

        String email = customer.email();
        String password = customer.password();

        return User
                .withUsername(email)
                .password(password)
                /*.roles("USER")*/ /*В Security filter нужно будет указывать hasRole()*/
                /*Если используете authorities(), тогда в Security filter нужно будет указывать*/
                .authorities(convertAuthorities(roles))
                .build();
    }

    private Set<GrantedAuthority> convertAuthorities(Set<UserRole> userRoles) {
        Set<GrantedAuthority> authorities=new HashSet<>();
        for (UserRole userRole : userRoles) {
            authorities.add(new SimpleGrantedAuthority(userRole.nameRole()));
        }
        return authorities;
    }
}
