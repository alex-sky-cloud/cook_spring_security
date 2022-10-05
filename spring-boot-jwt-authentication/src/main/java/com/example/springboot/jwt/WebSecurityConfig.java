package com.example.springboot.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class WebSecurityConfig {
	
	private final PasswordEncoder passwordEncoder;
	
	public WebSecurityConfig(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		return http
				.cors()
				.and()
				.csrf().disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				/*описываем как будут авторизовываться запросы*/
				.authorizeRequests(configurer ->
						configurer
								/*указываем api, на который не нужна авторизация.*/
								.antMatchers(
										"/error",
										"/login"
								)
								.permitAll()
								.anyRequest() /*все остальные запросы должны быть аутентифицированы*/
								.authenticated()
				)
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user1 = User
				.withUsername("user")
				.authorities("USER")
				.passwordEncoder(passwordEncoder::encode)
				.password("1234")
				.build();
		
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(user1);
		return manager;
	}
}
