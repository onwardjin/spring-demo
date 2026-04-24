package com.example.userorder.dto;

public record ErrorResponseDto(
        int status,
        String message
) {
}