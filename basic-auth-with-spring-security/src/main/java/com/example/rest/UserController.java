package com.example.rest;

import com.example.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("users")
    public User getUser(){
        return new User("Ann", "1", 40);
    }
}
