package com.example.userorder.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    private String loginId;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(){ }
    public User(String name, Integer age, String loginId, String password, Role role){
        this.name = name;
        this.age = age;
        this.loginId = loginId;
        this.password = password;
        this.role = role;
    }

    public Long getId(){ return id; }
    public String getName(){ return name; }
    public Integer getAge(){ return age; }
    public String getLoginId(){ return loginId; }
    public String getPassword(){ return password; }
    public Role getRole(){ return role; }

    public void setName(String name){ this.name = name; }
    public void setAge(Integer age){ this.age = age; }
    public void setRole(Role role){ this.role = role;}
}