package com.annotation.withannotation.web;

import jakarta.annotation.security.RolesAllowed;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
/*@EnableGlobalMethodSecurity(jsr250Enabled = true) - deprecated*/
@EnableMethodSecurity(prePostEnabled = false, jsr250Enabled = true)
public class ResourcesController {

    @RolesAllowed("USER")
    @GetMapping("protected")
    public String getUserResource(UsernamePasswordAuthenticationToken principal){

        String auth = getListAuthority(principal);

        return "/protected -> userName : " + principal.getName() + " - authorities : " + auth;
    }

    @RolesAllowed("USER")
    @GetMapping("user")
    public String getUserResourceMock(){


        return "user";
    }

    @RolesAllowed("ADMIN")
    @GetMapping("admin")
    public String getAdminResource(UsernamePasswordAuthenticationToken principal){

        String auth = getListAuthority(principal);

        return "/admin -> userName : " + principal.getName() + " - authorities : " + auth;
    }

    private String getListAuthority(UsernamePasswordAuthenticationToken principal){

        return principal
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
