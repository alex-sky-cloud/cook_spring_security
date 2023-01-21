package com.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests()
                .requestMatchers("/login", "/register", "/sampleInputs").permitAll()
                .requestMatchers("/resources/**", "/static/**").permitAll()
                .requestMatchers("/account/**").hasAuthority("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/home")
                //  .failureUrl("/handleLoginErrors").permitAll()
                .failureUrl("/login?error=true")/**/
                .and()
                .build();
    }

    /**
     * Здесь мы указали список Authentication providers, которые будут
     * зарегистрированы в {@link AuthenticationManager}
     * Ранее мы реализовали {@link UserDetailsService} -> {@link com.security.service.UserDetailsServiceImpl},
     * будет тем объектом, который мы передаем в конструктор {@link DaoAuthenticationProvider}
     * {@link UserDetailsService} - будет осуществлять поиск учетных данных пользователя,
     * в базе данных.
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService customUserDetailsService) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());

        List<AuthenticationProvider> providers =  List.of(authProvider);

        return new ProviderManager(providers);
    }

/*    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                // Spring Security should completely ignore URLs starting with /resources/
                .requestMatchers("/resources/**", "/static/**");
    }*/


    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
