package com.example.protect;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser(username = "john", roles = { "VIEWER" })
public class MockUserAtClassLevelIntegrationTest {

    @Configuration
    @ComponentScan("com.example.*")
    public static class SpringConfig {
    }
}
