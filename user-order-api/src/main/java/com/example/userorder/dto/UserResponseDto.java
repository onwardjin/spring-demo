package com.example.userorder.dto;

import com.example.userorder.entity.User;

public class UserResponseDto{
    private Long id;
    private String name;
    private Integer age;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.age = user.getAge();
    }

    public Long getId(){ return id; }
    public String getName(){ return name; }
    public Integer getAge(){ return age; }
}