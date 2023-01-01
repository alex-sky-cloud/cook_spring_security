package com.example.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@EnableMethodSecurity
@Slf4j
public class ResourcesController {

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("protected")
    public String getUserResource(UsernamePasswordAuthenticationToken principal) {

        String auth = getListAuthority(principal);

        return "/protected -> userName : " +
                principal.getName() +
                " - authorities : " + auth + " : principalName : " + getAuth();
    }

    private String getAuth() {

        String anonymousUser = "anonymous user";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return anonymousUser;
        }

        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            return authentication.getName();
        }

        return anonymousUser;
    }


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("user")
    public String getUserResourceMock(Principal principal) {

        String name = principal.getName();

        return "/user -> userName : " +
                name + " : principalName : " + getAuth();
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("user/auth")
    public String getUserResourceAuthenticationToken(Authentication authentication) {

        boolean authenticated = authentication.isAuthenticated();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Object credentials = authentication.getCredentials();
        Object details = authentication.getDetails();
        Object principal = authentication.getPrincipal();

        return "/user -> authorities : " + getAuthorities(authorities);
    }

    private String getAuthorities(Collection<? extends GrantedAuthority> authorities) {

        return authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("admin/auth")
    public String getAdminResourceAuthentication(Authentication principal) {

        UserDetails userDetails = (UserDetails) principal.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        log.info("User has authorities: " + getAuthorities(authorities));

        return "/admin -> User has authorities:" + getAuthorities(authorities);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("admin")
    public String getAdminResource(UsernamePasswordAuthenticationToken principal) {

        String auth = getListAuthority(principal);

        return "/admin -> userName : " +
                principal.getName() +
                " - authorities : " + auth + " : principalName : " + getAuth();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("admin/http")
    public String currentUserNameSimple(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        String name = principal.getName();

        return principal.getName();
    }

    private String getListAuthority(UsernamePasswordAuthenticationToken principal) {

        return principal
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
