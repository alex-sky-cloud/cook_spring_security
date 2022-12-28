package com.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class SecurityJavaConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/anonymous").anonymous()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/public").permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/deny").denyAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/authenticated").authenticated()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/has/authority/**").hasAuthority("ADMIN")
                .requestMatchers("/has/any/authority/**").hasAnyAuthority("ADMIN","USER")
                .and()
             /*   .authorizeHttpRequests()
                .requestMatchers("/has/role/**").hasRole("ADMIN")
                .requestMatchers("/has/any/role/**").hasAnyRole("ADMIN","USER")
                .and()*/
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();

    }

    @Bean
    @SuppressWarnings("unchecked")
    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {

        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            var authority = authorities
                    .iterator()
                    .next();

            return mappedAuthorities;
        };
    }

    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
