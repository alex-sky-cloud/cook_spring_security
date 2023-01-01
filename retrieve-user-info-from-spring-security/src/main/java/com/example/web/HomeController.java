package com.example.web;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("home")
    public String home() {
        return "home : principalName : " + getAuth();
    }

    private String getAuth(){

        String anonymousUser = "anonymous user";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            return anonymousUser;
        }

        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            return authentication.getName();
        }

        return anonymousUser;
    }
}
