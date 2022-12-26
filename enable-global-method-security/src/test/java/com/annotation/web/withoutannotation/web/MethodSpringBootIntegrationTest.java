package com.annotation.web.withoutannotation.web;

import com.annotation.withannotation.web.HomeController;
import com.annotation.withannotation.web.ResourcesController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class MethodSpringBootIntegrationTest {

    @Autowired
    private ResourcesController apiProtected;

    @Autowired
    private HomeController apiPublic;

    @Test
    @WithAnonymousUser
    void givenAnonymousUser_whenPublic_thenOk() {

        String result = apiPublic.home();

        assertThat(result).isEqualTo("home");
    }


    @WithMockUser(username="user", roles = "USER")
    @Test
    void givenUserWithRole_whenJsr250_thenOk() {

        String actualResult = apiProtected.getUserResourceMock();

        assertThat(actualResult).isEqualTo("user");
    }


    @WithMockUser(username="user", roles = "no-role")
    @Test
    void givenAnonymousUserWhenProtectedAccessDenied() {

        Assertions.assertThrowsExactly(
                AccessDeniedException.class,
                () -> apiProtected.getUserResourceMock()
        );
    }

}
