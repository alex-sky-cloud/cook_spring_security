package com.example.protect;

import com.example.annotation.IsViewer;
import com.example.model.CustomUser;
import com.example.repository.UserRoleRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserRolePreOrPostService {


    final UserRoleRepository userRoleRepository;


    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    public String getUsernameInLowerCase() {
        return getUsername().toLowerCase();
    }

    private String getUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication().getName();
    }

    @PreAuthorize("hasRole('ROLE_VIEWER') or hasRole('ROLE_EDITOR')")
    public boolean isValidUsernamePre(String username) {
        return userRoleRepository.isValidUsername(username);
    }

    @PreAuthorize("hasRole('ROLE_VIEWER')")
    public String getUsernameInUpperCase() {
        return getUserName().toUpperCase();
    }

    private String getUserName() {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext
                .getAuthentication()
                .getName();
    }

    @PreAuthorize("#username == authentication.principal.username")
    public String getMyRoles(String username) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    @PostAuthorize("#username == authentication.principal.username")
    public String getMyRolesPost(String username) {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.joining(","));
    }

    @PostAuthorize("returnObject.username == authentication.principal.nickName")
    public CustomUser loadUserDetail(String username) {
        return userRoleRepository.loadUserByUserName(username);
    }
}
