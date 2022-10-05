package com.example.authwithflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .formLogin().and()/*используем сгенерированную hmtl-форму от Spring для ввода учетных данных*/
                .httpBasic().disable() /*отключаем использование базовой аутентификации (HTTP Basic ) */
                .authorizeExchange()/*включаем пользовательскую авторизацию*/
                /*указываем какие методы http  и какие endpoints будут публично доступны*/
                .pathMatchers("/", "/login", "/signup").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/users").permitAll()
                /*указываем, с каким типом авторизации, будет доступен путь к группе endpoints /api/users/**" */
                .pathMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                /*включить аутентификацию для любых изменений*/
                .anyExchange().authenticated()
                .and()
                .build();
    }
}
