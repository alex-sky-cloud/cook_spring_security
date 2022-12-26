package com.example.protect;

import com.example.model.CustomUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
class UserRolePreOrPostServiceTest {

    @Configuration
    @ComponentScan("com.example.*")
    public static class SpringConfig {
    }

    @Test
    void loadUserDetail() {
    }

    @Autowired
    private UserRolePreOrPostService userRolePreOrPostService;


    @Test
    @WithUserDetails(
            value = "john",
            userDetailsServiceBeanName = "userDetailService")
    void whenJohn_callLoadUserDetail_thenOK() {

        CustomUser user = userRolePreOrPostService.loadUserDetail("jane");

        assertEquals("jane", user.getNickName());
    }

    @Test
    @WithMockUser(username = "JOHN", authorities = { "SYS_ADMIN" })
    void getUsernameInLowerCase() {

        String username = userRolePreOrPostService.getUsernameInLowerCase();

        assertEquals("john", username);
    }
}