package com.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class User {


    private Long id;


    private String username;

    private String password;

    private Set<Privilege> privileges;


    private Organization organization;


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("User [id=").append(id).append(", username=").append(username).append(", password=").append(password).append(", privileges=").append(privileges).append(", organization=").append(organization).append("]");
        return builder.toString();
    }

}
