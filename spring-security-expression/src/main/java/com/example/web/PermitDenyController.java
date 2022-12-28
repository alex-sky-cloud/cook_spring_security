package com.example.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PermitDenyController {

    @GetMapping("public")
    public String getPermitAllPublic(){

        return "public";
    }

    @GetMapping("deny")
    public String getDenyAllPublic(){

        return "deny";
    }
}
