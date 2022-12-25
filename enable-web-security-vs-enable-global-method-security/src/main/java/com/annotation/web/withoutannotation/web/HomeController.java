package com.annotation.web.withoutannotation.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("home")
    public String home() {
        return "home";
    }
}
