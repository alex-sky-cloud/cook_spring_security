package com.example.authwithflux.domain.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Builder
@Data
@Table("users")
public class User implements UserDetails {

    @Id
    private Long id;

    private String username;

    private String password;

    private String role;

    private Integer roleId;

    /**
     * Поле role может содержать в таблице строку-массив, в которой
     * типы полномочий, могут быть перечислены через запятую.
     * Данный метод сопоставляя такое поле, получает эту строку,
     * выделяет подстроки, разделенные запятой и создает из них
     * список полномочий, для конкретной учетной записи
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    /**
     * Утверждает, что время доступа для данной учетной записи, еще
     * не истекло
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Утверждает, что доступ для данной учетной записи, не заблокирован
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Утверждает, что время доступа `полномочий`, данной учетной записи, еще
     * не истекло
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Утверждает, что данная учетная запись активна.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
