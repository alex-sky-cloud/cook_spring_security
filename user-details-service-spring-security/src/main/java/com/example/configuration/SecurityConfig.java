package com.example.configuration;

import com.example.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfig {

   /**
     * bcrypt - это криптографическая хеш-функция,
     * разработанная Нильсом Провосом и Дэвидом Мазьером
     * на основе алгоритма шифрования Blowfish
     * int rounds = 12;
     * по умолчанию - 10.
     * Это степень надежности пароля
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        int rounds = 12;
        return new BCryptPasswordEncoder(rounds);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .httpBasic()
                .and()
                .authorizeHttpRequests()
                /*.requestMatchers("/user/**").hasRole("USER")*/
                .requestMatchers("/user/**", "/user/info/**").hasAuthority("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);;

        return http.build();
    }

    /**
     * Здесь мы указали список Authentication providers, которые будут
     * зарегистрированы в {@link AuthenticationManager}
     * Ранее мы реализовали {@link UserDetailsService} -> {@link CustomUserDetailService},
     * будет тем объектом, который мы передаем в конструктор {@link DaoAuthenticationProvider}
     * {@link UserDetailsService} - будет осуществлять поиск учетных данных пользователя,
     * в базе данных.
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService customUserDetailsService) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        List<AuthenticationProvider> providers =  List.of(authProvider);

        return new ProviderManager(providers);
    }
}
