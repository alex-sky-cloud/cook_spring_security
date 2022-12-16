package com.jwt.controller;

import com.jwt.configuration.jwt.TokenService;
import com.jwt.dtos.*;
import com.jwt.properties.TokenTimeLifeProperties;
import com.jwt.service.CustomUserDetailsService;
import com.jwt.service.UserRegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthRestController {

    private final TokenService tokenService;
    private final AuthenticationManager authManager;
    private final CustomUserDetailsService customUserDetailsService;

    private final UserRegistrationService userRegistrationService;

    private final TokenTimeLifeProperties tokenTimeLifeProperties;


    @PostMapping("/register")
    public ResponseEntity<LoginResponseRedirectDto> registerNewUser(@RequestBody LoginRequestRegistrationDto request) {

        userRegistrationService.registerUser(request);

        LoginResponseRedirectDto redirectDto = new LoginResponseRedirectDto(
                request.username(),
                request.password(),
                request.role(),
                "/login");

        return ResponseEntity.ok(redirectDto);
    }

    /**
     * формируется accessToken и refreshToken, и передается клиенту.
     */
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.username(), request.password());

        authManager.authenticate(authenticationToken);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.username());

        Integer timeLifeAccessToken = tokenTimeLifeProperties.getAccess();
        Integer timeLifeRefreshToken = tokenTimeLifeProperties.getRefresh();

        String accessToken = tokenService.generateAccessToken(userDetails, timeLifeAccessToken);
        String refreshToken = tokenService.generateRefreshToken(userDetails, timeLifeRefreshToken);

        String message = "User with email : " + request.username() + " successfully logged in !";

        return new LoginResponseDto(message, accessToken, refreshToken);
    }

    @GetMapping("/token/refresh")
    public RefreshTokenResponseDto refreshToken(HttpServletRequest request) {

        String headerAuth = request.getHeader("Authorization");

        int indexStartSubString = 7;
        String refreshToken = headerAuth.substring(indexStartSubString);

        String email = tokenService.parseToken(refreshToken);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        Integer timeLifeAccessToken = tokenTimeLifeProperties.getAccess();
        Integer timeLifeRefreshToken = tokenTimeLifeProperties.getRefresh();
        String accessToken = tokenService.generateAccessToken(userDetails, timeLifeAccessToken);
        String refreshTokenGenerated = tokenService.generateRefreshToken(userDetails, timeLifeRefreshToken);

        return new RefreshTokenResponseDto(accessToken, refreshTokenGenerated);
    }
}
