package com.example.userorder.dto;

import com.example.userorder.entity.User;

public class UserResponseDto {
    private String name;
    private Integer age;

    public UserResponseDto(User user){
        this.name = user.getName();
        this.age = user.getAge();
    }

    public String getName(){ return name; }
    public Integer getAge(){ return age; }
}
