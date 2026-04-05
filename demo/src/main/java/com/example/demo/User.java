package com.example.demo;

public class User {
    private String name;
    private int age;

    public User(String name, int age){
        this.name = name;
        this.age = age;
    }

    public String getName(){ return name; }
    public int getAge(){ return age; }
}

/*
- 내부에서 사용할 사용자 데이터 표현
- 서비스 로직에서 다룰 기본 객체
- 요청/응답 객체와 분리
 */