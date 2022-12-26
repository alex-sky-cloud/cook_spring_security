package com.example;



import java.util.ArrayList;
import java.util.List;

import com.example.protect.UserRoleSecuredService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class MethodSecurityIntegrationTest {

    @Autowired
    UserRoleSecuredService userRoleSecuredService;

    @Configuration
    @ComponentScan("com.example.*")
    public static class SpringConfig {

    }

    //(expected = AuthenticationCredentialsNotFoundException.class)
    @Test
    void givenNoSecurity_whenCallGetUsername_thenReturnException() {
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
    @WithMockUser(username = "john", roles = { "EDITOR" })
    void givenUsernameJohn_whenCallIsValidUsername_thenReturnTrue() {
        boolean isValid = userRoleSecuredService.isValidUsername("john");
        assertEquals(true, isValid);
    }

    //(expected = AccessDeniedException.class)
    @Test
    @WithMockUser(username = "john", roles = { "ADMIN" })
    void givenRoleAdmin_whenCallGetUsername_thenReturnAccessDenied() {
        userRoleSecuredService.getUsername();
    }

    //(expected = AccessDeniedException.class)
    @Test
    @WithMockUser(username = "john", roles = { "USER" })
    public void givenRoleUser_whenCallGetUsername2_thenReturnAccessDenied() {
        userRoleSecuredService.getUsername2();
    }

    @Test
    @WithMockUser(username = "john", roles = { "VIEWER", "EDITOR" })
    public void givenRoleViewer_whenCallGetUsername2_thenReturnUsername() {
        String userName = userRoleSecuredService.getUsername2();
        assertEquals("john", userName);
    }

    @Test
    @WithMockUser(username = "john", roles = { "VIEWER" })
    public void givenUsernameJerry_whenCallIsValidUsername2_thenReturnFalse() {
        boolean isValid = userRoleSecuredService.isValidUsername2("jerry");
        assertEquals(false, isValid);
    }

    @Test
    @WithMockUser(username = "JOHN", authorities = { "SYS_ADMIN" })
    public void givenAuthoritySysAdmin_whenCallGetUsernameLC_thenReturnUsername() {
        String username = userRoleSecuredService.getUsernameLC();
        assertEquals("john", username);
    }

    @Test
    @WithMockUser(username = "john", roles = { "ADMIN", "USER", "VIEWER" })
    public void givenUserJohn_whenCallGetMyRolesWithJohn_thenReturnRoles() {
        String roles = userRoleSecuredService.getMyRoles("john");
        assertEquals("ROLE_ADMIN,ROLE_USER,ROLE_VIEWER", roles);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "john", roles = { "ADMIN", "USER", "VIEWER" })
    public void givenUserJane_whenCallGetMyRolesWithJane_thenAccessDenied() {
        userRoleSecuredService.getMyRoles("jane");
    }
    
    @Test
    @WithMockUser(username = "john", roles = { "ADMIN", "USER", "VIEWER" })
    public void givenUserJohn_whenCallGetMyRoles2WithJohn_thenReturnRoles() {
        String roles = userRoleSecuredService.getMyRoles2("john");
        assertEquals("ROLE_ADMIN,ROLE_USER,ROLE_VIEWER", roles);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "john", roles = { "ADMIN", "USER", "VIEWER" })
    public void givenUserJane_whenCallGetMyRoles2WithJane_thenAccessDenied() {
        userRoleSecuredService.getMyRoles2("jane");
    }

    @Test(expected = AccessDeniedException.class)
    @WithAnonymousUser
    public void givenAnomynousUser_whenCallGetUsername_thenAccessDenied() {
        userRoleSecuredService.getUsername();
    }

    @Test
    @WithMockJohnViewer
    public void givenMockedJohnViewer_whenCallGetUsername_thenReturnUsername() {
        String userName = userRoleSecuredService.getUsername();
        assertEquals("john", userName);
    }

    @Test
    @WithMockUser(username = "jane")
    public void givenListContainCurrentUsername_whenJoinUsernames_thenReturnUsernames() {
        List<String> usernames = new ArrayList<>();
        usernames.add("jane");
        usernames.add("john");
        usernames.add("jack");
        String containCurrentUser = userRoleSecuredService.joinUsernames(usernames);
        assertEquals("john;jack", containCurrentUser);
    }

    @Test
    @WithMockUser(username = "john")
    public void givenListContainCurrentUsername_whenCallJoinUsernamesAndRoles_thenReturnUsernameAndRoles() {
        List<String> usernames = new ArrayList<>();
        usernames.add("jane");
        usernames.add("john");
        usernames.add("jack");

        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_TEST");

        String containCurrentUser = userRoleSecuredService.joinUsernamesAndRoles(usernames, roles);
        assertEquals("jane;jack:ROLE_ADMIN;ROLE_TEST", containCurrentUser);
    }

    @Test
    @WithMockUser(username = "john")
    public void givenUserJohn_whenCallGetAllUsernamesExceptCurrent_thenReturnOtherusernames() {
        List<String> others = userRoleSecuredService.getAllUsernamesExceptCurrent();
        assertEquals(2, others.size());
        assertTrue(others.contains("jane"));
        assertTrue(others.contains("jack"));
    }

    @Test
    @WithMockUser(username = "john", roles = { "VIEWER" })
    public void givenRoleViewer_whenCallGetUsername4_thenReturnUsername() {
        String userName = userRoleSecuredService.getUsername4();
        assertEquals("john", userName);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(username = "john")
    public void givenDefaultRole_whenCallGetUsername4_thenAccessDenied() {
        userRoleSecuredService.getUsername4();
    }

}