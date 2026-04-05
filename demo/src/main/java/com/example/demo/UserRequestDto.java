package com.example.demo;

public class UserRequestDto {
    private String name;
    private int age;

    public String getName(){ return name; }
    public int getAge(){ return age; }
}

/*
- 클라이언트가 보낸 JSON을 받는 용도
 */