package com.example.userorder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderCreateRequestDto(
        @NotBlank(message = "Product name is required")
        String productName,

        @Min(0) @NotNull(message = "Quantity is required")
        Integer quantity,

        @Min(0) @NotNull(message = "Price is required")
        Integer price
) {
}