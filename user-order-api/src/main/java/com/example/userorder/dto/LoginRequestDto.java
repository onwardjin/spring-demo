package com.example.userorder.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {
    @NotBlank(message = "Login ID is required")
    private String loginId;

    @NotBlank(message = "Password is required")
    private String password;

    public String getLoginId(){ return loginId; }
    public String getPassword(){ return password; }
}
