package com.jwt.dtos;

public record LoginResponseRedirectDto(String username, String password, String role, String uriRedirect) {
}
