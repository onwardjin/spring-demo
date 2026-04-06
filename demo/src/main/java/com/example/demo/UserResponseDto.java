package com.example.demo;

public class UserResponseDto{
    private String name;
    private int age;

    UserResponseDto(String name, int age){
        this.name = name;
        this.age = age;
    }

    public String getName(){ return name; }
    public int getAge(){ return age; }
}