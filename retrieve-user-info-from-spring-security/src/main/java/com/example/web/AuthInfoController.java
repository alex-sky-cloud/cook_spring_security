package com.example.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthInfoController {

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/auth")
    public String getAuth(){
        return "auth";
    }
}
