package com.reg.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/register",
                        "/plugins/**",
                        "/dist/**",
                        "/js/**").permitAll()
                .anyRequest().authenticated()
               /* .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().permitAll()*/
                .and()
               /* .httpBasic()
                .and()*/
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);;

        return http.build();
    }


    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
