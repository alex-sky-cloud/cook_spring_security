package com.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class CredentialsService {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(encoder().encode("admin"))
                .authorities("ADMIN")
               //.roles("ADMIN")
                .build();
        UserDetails user = User.withUsername("user")
                .password(encoder().encode("user"))
               .authorities("USER")
               // .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }
}
