package com.example.userorder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRequestDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Age is required")
    @Min(0)
    private Integer age;

    @NotBlank(message = "Login ID is required")
    private String loginId;

    @NotBlank(message = "Password is required")
    private String password;

    public String getName(){ return name; }
    public Integer getAge(){ return age; }
    public String getLoginId(){ return loginId; }
    public String getPassword(){ return password; }
}