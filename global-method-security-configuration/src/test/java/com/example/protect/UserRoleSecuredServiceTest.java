package com.example.protect;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class UserRoleSecuredServiceTest {


    @Configuration
    @ComponentScan("com.example.*")
    public static class SpringConfig {
    }

    @Autowired
    private UserRoleSecuredService userRoleSecuredService;

    @Test
    @WithMockJohnViewer
    void givenMockedJohnViewer_whenCallGetUsername_thenReturnUsername() {
        String userName = userRoleSecuredService.getUsername();

        assertEquals("john", userName);
    }

    @Test
    @WithMockUser(username = "john", roles = { "VIEWER" })
    void givenRoleViewer_whenCallGetUsername_thenReturnUsername() {

        String userName = userRoleSecuredService.getUsername();

        assertEquals("john", userName);
    }

    @Test
    @WithAnonymousUser
    void givenAnomynousUser_whenCallGetUsername_thenAccessDenied() {
        assertThrowsExactly(AccessDeniedException.class, () -> userRoleSecuredService.getUsername());
    }

    @Test
    @WithMockUser(username = "john", roles = { "VIEWER-wrong" })
    void givenRoleViewer_whenCallGetUsername_accessDenied() {

        assertThrowsExactly(AccessDeniedException.class, () -> userRoleSecuredService.getUsername());

    }


}