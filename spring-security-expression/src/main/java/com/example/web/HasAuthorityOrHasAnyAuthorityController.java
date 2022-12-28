package com.example.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HasAuthorityOrHasAnyAuthorityController {

    @GetMapping("has/authority")
    public String getProtectedHasAuthority(UsernamePasswordAuthenticationToken principal){
        log.info("username : " + principal.getName() +
                " --- authorities :" + principal.getAuthorities().toString()
        );
        return "hasAuthority";
    }

    @GetMapping("has/any/authority")
    public String getProtectedHasAnyAuthority(){
        return "hasAnyAuthority";
    }
}
