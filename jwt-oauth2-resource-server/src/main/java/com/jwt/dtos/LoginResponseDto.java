package com.jwt.dtos;

public record LoginResponseDto(String message, String accessJwtToken, String refreshJwtToken) {
}
