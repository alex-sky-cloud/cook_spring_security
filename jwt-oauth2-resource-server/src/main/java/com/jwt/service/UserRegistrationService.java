package com.jwt.service;

import com.jwt.dtos.LoginRequestRegistrationDto;
import com.jwt.model.Role;
import com.jwt.model.User;
import com.jwt.repository.RoleRepository;
import com.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder bcryptEncoder;

    @Transactional
    public void registerUser(LoginRequestRegistrationDto request) {

        String userEmail = request.username();

        boolean userExists = userRepository
                .findByEmail(userEmail)
                .isPresent();
        if(userExists){
            throw new RuntimeException("User with email = " + userEmail + " exist!");
        }

        User user = new User();
        user.setEmail(userEmail);
        String password = request.password();
        user.setPassword(bcryptEncoder.encode(password));

        String roleName = request.role();

        Role role = roleRepository
                .findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role with name : " + roleName + "doesn't exist."));
        user
                .getRoles()
                .add(role);

        userRepository.save(user);
    }
}
