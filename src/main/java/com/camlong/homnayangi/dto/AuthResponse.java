package com.camlong.homnayangi.dto;

public record AuthResponse(Long userId, String username, String token, String refreshToken) {
}
