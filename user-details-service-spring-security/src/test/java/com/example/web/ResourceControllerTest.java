package com.example.web;

import com.example.service.UserDetailsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ResourceControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    void getInfoUser() {

        ResponseEntity<UserDetailsDto> result = template
                .withBasicAuth("mail1@mail.com", "user1")
                .getForEntity("/user/info", UserDetailsDto.class);

        UserDetailsDto resultBody = result.getBody();
        String actualResult = resultBody.username();

        int statusCode = result.getStatusCode().value();

        assertEquals(200, statusCode);

        assertThat(actualResult, containsStringIgnoringCase("mail1@mail.com"));
    }
}