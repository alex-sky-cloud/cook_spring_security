package com.example.web;

import com.example.configuartion.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class ResourceAuthFromFacadeController {

    private final AuthenticationFacade facade;

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("protected/facade")
    public String currentUserNameSimple() {

        Authentication authentication = facade.getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean authenticated = authentication.isAuthenticated();
        Object credentials = authentication.getCredentials();
        Object principal = authentication.getPrincipal();

        return authentication.getName();
    }
}
