package com.example.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;

import java.util.stream.Stream;

/**
 * Использование @WithMockUser в данном случае не позволит проверить правильность
 * фильтров безопасности(его использование будет корректно, когда используется аннотированный
 * подход), так как нам нужно проверить сооветствие учетных данных, которые настроены in-memory
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HasRoleOrHasAnyRoleTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @WithAnonymousUser
    void getPublicHello() {
    }

    @Test
    void getProtectedHasRole(){

        ResponseEntity<String> result = restTemplate
                .withBasicAuth("admin", "admin")
                .getForEntity("/has/role", String.class);

        String actualResult = result.getBody();

        Assertions.assertEquals(actualResult, "hasRole");
    }

    @ParameterizedTest
    @MethodSource("credentialsFactory")
    void getProtectedHasAnyRole(User credentials) {

        ResponseEntity<String> result = restTemplate
                .withBasicAuth(credentials.userName, credentials.password)
                .getForEntity("/has/any/role", String.class);

        String actualResult = result.getBody();

        Assertions.assertEquals(actualResult, "hasAnyRole");
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