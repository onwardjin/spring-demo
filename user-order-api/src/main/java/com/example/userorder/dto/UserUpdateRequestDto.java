package com.example.userorder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRequestDto(
        @NotBlank(message = "Name is required")
        String name,

        @Min(0) @NotNull(message = "Age is required")
        Integer age
) {
}