package com.example.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnonymousRememberMeAuthenticatedFullyAuthenticated {

    @GetMapping("anonymous")
    public String getAnonymous() {

        return "anonymous";
    }

    @GetMapping("authenticated")
    public String getAuthenticated() {

        return "authenticated";
    }
}
