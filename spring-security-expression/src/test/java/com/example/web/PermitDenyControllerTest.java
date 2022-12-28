package com.example.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PermitDenyControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @WithAnonymousUser
    void getPermitAllPublic() {

        ResponseEntity<String> response = restTemplate.getForEntity("/public", String.class);
        String actual = response.getBody();

        Assertions.assertEquals("public", actual);
    }

    @Test
    void getDenyAllPublic() {


        ResponseEntity<AccessDeniedException> response = restTemplate
                .withBasicAuth("admin", "admin")
                .getForEntity("/deny", AccessDeniedException.class);

        HttpStatusCode actualStatusCode = response.getStatusCode();

        Assertions.assertEquals(HttpStatus.FORBIDDEN, actualStatusCode);
    }
}