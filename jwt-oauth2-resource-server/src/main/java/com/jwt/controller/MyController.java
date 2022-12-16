package com.jwt.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MyController {

    @GetMapping("/admin")
    public String homeAdmin( Principal principal) {
        return "Hello mr. " + principal.getName();
    }

    @GetMapping("/user")
    public String homeUser(Principal principal) {
        return "Hello mr. " + principal.getName();
    }

    @GetMapping("/user2")
    public String homeUser2(Principal principal) {
        return "Hello mr. " + principal.getName();
    }
}
