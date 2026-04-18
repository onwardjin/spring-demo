package com.example.userorder.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderRequestDto{
    @NotBlank(message = "Item is required")
    private String item;

    public String getItem(){ return item; }
}
