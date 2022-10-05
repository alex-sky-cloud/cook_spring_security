package com.example.springboot.jwt.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.springboot.jwt.WebSecurityConfig;
import com.example.springboot.jwt.controller.resource.LoginResult;
import com.example.springboot.jwt.security.JwtHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * The auth controller to handle login requests
 */
@CrossOrigin(origins = {"${app.security.cors.origin}"})
@RestController
public class AuthController {
	
	private final JwtHelper jwtHelper;
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	
	public AuthController(JwtHelper jwtHelper, UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		this.jwtHelper = jwtHelper;
		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
	}
	@PostMapping(path = "login", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public LoginResult login(
			@RequestParam String username,
			@RequestParam String password) {
		
		UserDetails userDetails;

		/** Запрашиваем из базы данные о пользователе (по имени). В данном случае,
		 * это всего лишь один пользователь, которого мы
		 * создали искусственно
		 * в классе {@link WebSecurityConfig#userDetailsService()}  },
		 * поэтому в данном случае, данные его хранятся в оперативной памяти.
		 * */
		try {
			userDetails = userDetailsService.loadUserByUsername(username);
		} catch (UsernameNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
		}

		/** После того как был получен principal объект найденного пользователя,
		 * теперь мы сравниваем пароль введенный в форме ввода, на соответствие с паролем,
		 * который был назначен данному пользователю.
		 * Если пароль правильный, тогда используя полученные данные из формы,
		 * добавляем 'claims', которые будут использованы в payload для создаваемого
		 * JWT
		 * Далее, создаем JWT и передаем его как аргумент в  {@link LoginResult},
		 * который будет обработан как response и на основе этих данных,
		 * клиенту будет возвращен сгенерированный токен c помощью которого, клиент
		 * может получать защищенные ресурсы
		 */
		if (passwordEncoder.matches(password, userDetails.getPassword())) {
			Map<String, String> claims = new HashMap<>();
			claims.put("username", username);
			
			String authorities = userDetails
					.getAuthorities()
					.stream()
					.map(GrantedAuthority::getAuthority)
					.collect(Collectors.joining(" "));
			claims.put("authorities", authorities);
			claims.put("userId", String.valueOf(1));
			
			String jwt = jwtHelper.createJwtForClaims(username, claims);
			return new LoginResult(jwt);
		}
		
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
	}
}
