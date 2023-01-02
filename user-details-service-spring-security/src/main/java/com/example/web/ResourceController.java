package com.example.web;

import com.example.model.CustomerModel;
import com.example.service.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final UserDetailsService userDetailsService;

    @GetMapping("user/info")
    public UserDetailsDto getInfoUser(){

        String email = "mail" + 1 + "@mail.com";
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        return convertToDto(userDetails);
    }

      private UserDetailsDto convertToDto(UserDetails userDetails){

        String authorities = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String username = userDetails.getUsername();

        return new UserDetailsDto(username, authorities);
    }
}
