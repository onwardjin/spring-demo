package com.example.userorder.dto;

import com.example.userorder.entity.Order;

public record OrderResponseDto(
        Long id,
        String productName,
        Integer quantity,
        Integer price
) {
    public OrderResponseDto(Order order) {
        this(
                order.getId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice()
        );
    }
}
