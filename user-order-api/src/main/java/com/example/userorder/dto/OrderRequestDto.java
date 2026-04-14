package com.example.userorder.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderRequestDto {
    @NotBlank(message = "Name is required")
    private String item;
    private Long userId;

    public String getItem(){ return item; }
    public Long getUserId(){ return userId; }
}
