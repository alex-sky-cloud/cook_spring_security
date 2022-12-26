package com.example.protect;

import com.example.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserRoleSecuredService {

    final UserRoleRepository userRoleRepository;

    @Secured({ "ROLE_VIEWER", "ROLE_EDITOR" })
    public boolean isValidUsername(String username) {
        return userRoleRepository.isValidUsername(username);
    }

    @Secured("ROLE_VIEWER")
    public String getUsername() {

        SecurityContext securityContext = SecurityContextHolder.getContext();

        return securityContext
                .getAuthentication()
                .getName();
    }



}
