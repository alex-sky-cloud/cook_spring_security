package com.example.protect;

import com.example.model.CustomUser;
import com.example.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserRoleMultiSecurityAnnotationService {

    final UserRoleRepository userRoleRepository;

    @PreAuthorize("#username == authentication.principal.username")
    @PostAuthorize("returnObject.username == authentication.principal.nickName")
    public CustomUser securedLoadUserDetail(String username) {
        return userRoleRepository.loadUserByUserName(username);
    }
}
