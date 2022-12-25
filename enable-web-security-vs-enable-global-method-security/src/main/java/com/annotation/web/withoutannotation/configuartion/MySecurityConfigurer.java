package com.annotation.web.withoutannotation.configuartion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MySecurityConfigurer {


    /**
     * Используется DSL HttpSecurity
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .httpBasic();

        return http.build();
    }

        /**
         * org.springframework.security.config.annotation.web.builders.WebSecurity [WARN ]  -
         * You are asking Spring Security to ignore Mvc [pattern='/home/**'].
         * This is not recommended -- please use permitAll via HttpSecurity#authorizeHttpRequests instead.
         * org.springframework.security.web.DefaultSecurityFilterChain [INFO ]  -
         * Will not secure Mvc [pattern='/home/**']
         */
        @Bean
        public WebSecurityCustomizer ignoreResources () {
            return webSecurity -> webSecurity
                    .ignoring()
                    .requestMatchers("/home/**");
        }
    }
