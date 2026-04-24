package com.example.userorder.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "Login ID is required")
        String loginId,

        @NotBlank(message = "password is required")
        String password
) {
}