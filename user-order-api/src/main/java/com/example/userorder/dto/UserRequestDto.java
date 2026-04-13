package com.example.userorder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRequestDto{
    @NotBlank(message = "Name is required")
    private String name;
    @NotNull(message = "Age is required")
    private Integer age;
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;

    public String getName(){ return name; }
    public Integer getAge(){ return age; }
    public String getLoginId(){ return loginId; }
    public String getPassword(){ return password; }
}