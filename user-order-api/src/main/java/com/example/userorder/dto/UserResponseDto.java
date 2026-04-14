package com.example.userorder.dto;

import com.example.userorder.entity.User;

public class UserResponseDto {
    private String name;
    private Integer age;
    private String loginId;

    public UserResponseDto(User user){
        this.name = user.getName();
        this.age = user.getAge();
        this.loginId = user.getLoginId();
    }

    public String getName(){ return name; }
    public Integer getAge(){ return age; }
    public String getLoginId(){ return loginId; }
}
