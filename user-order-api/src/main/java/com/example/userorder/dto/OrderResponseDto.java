package com.example.userorder.dto;

import com.example.userorder.entity.Order;

public class OrderResponseDto {
    private String item;
    private Long userId;

    public OrderResponseDto(Order order){
        this.item = order.getItem();
        this.userId = order.getUser().getId();
    }

    public String getItem(){ return item; }
    public Long getUserId(){ return userId; }
}
