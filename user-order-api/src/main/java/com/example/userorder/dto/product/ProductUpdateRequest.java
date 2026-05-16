package com.example.userorder.dto.product;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductUpdateRequest(
        String name,

        @Positive
        Long unitPrice,

        @PositiveOrZero
        Integer stockQuantity
) {
}