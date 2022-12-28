package com.example.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HasRoleOrHasAnyRoleController {

    @GetMapping("hello")
    public String getPublicHello(){
        return "hello";
    }

    @GetMapping("has/role")
    public String getProtectedHasRole(UsernamePasswordAuthenticationToken principal){
        log.info("username : " + principal.getName() +
                " --- authorities :" + principal.getAuthorities().toString()
        );
        return "hasRole";
    }

    @GetMapping("has/any/role")
    public String getProtectedHasAnyRole(){
        return "hasAnyRole";
    }
}
