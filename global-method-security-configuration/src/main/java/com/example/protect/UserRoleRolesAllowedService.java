package com.example.protect;

import com.example.repository.UserRoleRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserRoleRolesAllowedService {

    final UserRoleRepository userRoleRepository;

    @RolesAllowed("ROLE_VIEWER")
    public String getUsernameRolesAllowed() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication().getName();
    }

    @RolesAllowed({ "ROLE_VIEWER", "ROLE_EDITOR" })
    public boolean isValidUsernameRolesAllowed(String username) {
        return userRoleRepository.isValidUsername(username);
    }
}
