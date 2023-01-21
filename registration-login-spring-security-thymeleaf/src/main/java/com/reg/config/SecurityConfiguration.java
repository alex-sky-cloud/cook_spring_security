package com.reg.config;


import com.reg.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserService userService) {

		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService);
		authProvider.setPasswordEncoder(passwordEncoder());

		List<AuthenticationProvider> providers =  List.of(authProvider);

		return new ProviderManager(providers);
	}

	/**
	 * invalidateHttpSession(true) - Настраивает SecurityContextLogoutHandler
	 * для аннулирования HttpSession во время выхода из системы.
	 * Таким образом. При выходе из системы, HttpSession становится недействительным.
	 *
	 * clearAuthentication(true)   Указывает, должен ли SecurityContextLogoutHandler сбрасывать
	 * Authentication во время выхода из системы.
	 */
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		return http
				.csrf()
				.and()
				.authorizeHttpRequests()
				.requestMatchers(
						"/registration**",
						"/js/**",
						"/css/**",
						"/img/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login").permitAll()
				.and()
				.logout()
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout")
				.permitAll()
				.and()
				.build();
	}
}
