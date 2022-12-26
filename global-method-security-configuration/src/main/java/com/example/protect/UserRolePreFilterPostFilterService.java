package com.example.protect;

import com.example.repository.UserRoleRepository;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserRolePreFilterPostFilterService {

    final UserRoleRepository userRoleRepository;

    @PostFilter("filterObject != authentication.principal.username")
    public List<String> getAllUsernamesExceptCurrent() {

        return userRoleRepository.getAllUsernames();
    }

    @PreFilter("filterObject != authentication.principal.username")
    public String joinUsernames(List<String> usernames) {
        return String.join(";", usernames);
    }


    @PreFilter(value = "filterObject != authentication.principal.username", filterTarget = "usernames")
    public String joinUsernamesAndRoles(List<String> usernames, List<String> roles) {
        return
                String
                        .join(";", usernames) + ":" +
                        String
                                .join(";", roles);
    }
}
