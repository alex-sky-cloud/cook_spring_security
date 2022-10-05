package com.example.springboot.jwt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = {"${app.security.cors.origin}"})
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserDetailsService userDetailsService;

	@GetMapping
	public UserDetails getUser(Authentication authentication) {

		JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;

		Map<String, Object> tokenPayloads = token
				.getTokenAttributes();

		String userNameFromTokenPayloads = tokenPayloads
				.get("username")
				.toString();

		return userDetailsService
				.loadUserByUsername(userNameFromTokenPayloads);
	}
}
