package com.jwt.dtos;

public record RefreshTokenResponseDto(String accessJwtToken, String refreshJwtToken) {
}
