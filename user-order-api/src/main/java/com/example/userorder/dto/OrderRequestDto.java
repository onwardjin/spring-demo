package com.example.userorder.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderRequestDto {
    @NotBlank(message = "Name is required")
    private String name;
    private Long userId;

    public String getName(){ return name; }
    public Long getUserId(){ return userId; }
}
