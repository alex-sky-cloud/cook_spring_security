package com.example.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnonymousRememberMeAuthenticatedFullyAuthenticatedTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAnonymous() {

        ResponseEntity<String> result = restTemplate
                .getForEntity("/anonymous", String.class);

        String actualResult = result.getBody();

        Assertions.assertEquals(actualResult, "anonymous");
    }


    @Test
    void getAuthenticated() {

        ResponseEntity<String> result = restTemplate
                .withBasicAuth("user", "user")
                .getForEntity("/authenticated", String.class);

        String actualResult = result.getBody();

        Assertions.assertEquals(actualResult, "authenticated");
    }
}