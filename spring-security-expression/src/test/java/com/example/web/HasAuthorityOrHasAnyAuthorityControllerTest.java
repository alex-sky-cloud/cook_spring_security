package com.example.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.stream.Stream;



/**
 * Использование @WithMockUser в данном случае не позволит проверить правильность
 * фильтров безопасности(его использование будет корректно, когда используется аннотированный
 * подход), так как нам нужно проверить сооветствие учетных данных, которые настроены in-memory
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HasAuthorityOrHasAnyAuthorityControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getProtectedHasAuthority() {

        ResponseEntity<String> result = restTemplate
                .withBasicAuth("admin", "admin")
                .getForEntity("/has/authority", String.class);

        String actualResult = result.getBody();

        Assertions.assertEquals(actualResult, "hasAuthority");
    }


    @Test
    void getProtectedHasAuthorityFailed() {


        ResponseEntity<AccessDeniedException> response = restTemplate
                .withBasicAuth("wrong-cred", "admin")
                .getForEntity("/has/authority", AccessDeniedException.class);

        HttpStatusCode statusCodeActual = response.getStatusCode();

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, statusCodeActual);

    }

    @ParameterizedTest
    @MethodSource("credentialsFactory")
    void getProtectedHasAnyAuthority(User credentials) {

        ResponseEntity<String> result = restTemplate
                .withBasicAuth(credentials.userName, credentials.password)
                .getForEntity("/has/any/authority", String.class);

        String actualResult = result.getBody();

        Assertions.assertEquals(actualResult, "hasAnyAuthority");
    }

    static Stream<User> credentialsFactory() {

        return Stream.of(
                new User("admin", "admin"),
                new User("user", "user")
        );
    }

    private record User(String userName, String password) {
    }
}