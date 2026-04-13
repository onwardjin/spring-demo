package com.example.userorder.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class OrderRequestDto {
    @NotBlank(message = "Item is required")
    private String item;
    @NotNull(message = "User ID is required")
    private Long userId;

    public String getItem(){
        return item;
    }
    public Long getUserId(){
        return userId;
    }
}