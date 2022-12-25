package com.annotation.web.withoutannotation.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ResourcesControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    void getUserResource() {

        ResponseEntity<String> result = template
                .withBasicAuth("user", "user")
                .getForEntity("/protected", String.class);

        String actualResult = result.getBody();

        int statusCode = result.getStatusCode().value();

        assertEquals(200, statusCode);

        assertThat(actualResult, containsStringIgnoringCase("protected"));

    }

    @Test
    void getAdminResource() {

        ResponseEntity<String> result = template
                .withBasicAuth("admin", "admin")
                .getForEntity("/admin", String.class);

        String actualResult = result.getBody();

        int statusCode = result.getStatusCode().value();

        assertEquals(200, statusCode);

        assertThat(actualResult, containsStringIgnoringCase("admin"));
    }

    @Test
    void getFailedAdminResourceWithWrongCredentials() {

        ResponseEntity<String> result = template
                .withBasicAuth("user", "user")
                .getForEntity("/admin", String.class);


        HttpStatusCode statusCode = result.getStatusCode();

        assertEquals(HttpStatus.FORBIDDEN, statusCode);
    }

    @Test
    void getFailedAdminResourceWithAnonymousUser() {

        ResponseEntity<String> result = template
                .getForEntity("/admin", String.class);


        HttpStatusCode statusCode = result.getStatusCode();

        assertEquals(HttpStatus.UNAUTHORIZED, statusCode);
    }

    @Test
    void home() {

        ResponseEntity<String> result = template.getForEntity("/home", String.class);

        String actualResult = result.getBody();

        int statusCode = result.getStatusCode().value();

        assertEquals(200, statusCode);
        assertThat(actualResult, containsStringIgnoringCase("home"));
    }
}