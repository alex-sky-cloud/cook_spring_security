package com.reg.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginPageController {

    public static final String LAST_USERNAME_KEY = "LAST_USERNAME";

    private final UserDetailsService customerAccountService;

    @GetMapping
    public String login(){

        return "account/login";
    }

}
