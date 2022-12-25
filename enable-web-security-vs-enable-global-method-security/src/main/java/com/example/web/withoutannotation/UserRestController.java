package com.example.web.withoutannotation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @GetMapping("protected")
    public String getAdminResource(){
        return "protected";
    }
}
