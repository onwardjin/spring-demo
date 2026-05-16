package com.example.userorder.dto.order;

public record OrderSearchCondition(
        long minPrice,
        long maxPrice
) {
}