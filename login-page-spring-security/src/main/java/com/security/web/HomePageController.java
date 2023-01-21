package com.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/home")
@Controller
public class HomePageController {

    @GetMapping
    public String geHome(){
        return "home";
    }

}
