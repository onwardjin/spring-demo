package com.example.userorder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;

    @Column(unique = true)
    private String loginId;
    private String password;

    public User(){ }
    public User(
            String name,
            Integer age,
            String loginId,
            String password
            ){
        this.name = name;
        this.age = age;
        this.loginId = loginId;
        this.password = password;
    }

    public Long getId(){ return id; }
    public String getName(){ return name; }
    public Integer getAge(){ return age; }
    public String getLoginId(){ return loginId; }
    public String getPassword(){ return password; }

    public void setName(String name){ this.name = name; }
    public void setAge(Integer age){ this.age = age; }
}
