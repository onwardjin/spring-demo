package com.example.demo;

// Response 요청용
public class UserResponseDto {
    private String name;
    private int age;

    public UserResponseDto(String name, int age){
        this.name = name;
        this.age = age;
    }

    public String getName(){ return name; }
    public int getAge(){ return age; }
}

/*
- 서버가 클라이언트에게 돌려줄 데이터 형식 정의
 */