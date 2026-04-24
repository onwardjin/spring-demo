package com.example.userorder.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String loginId;
    private String password;

    private String name;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();


    private User(String loginId, String password, String name, Integer age, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.age = age;
        this.role = role;
    }

    public static User createGeneralUser(String loginId, String password, String name, Integer age) {
        return new User(loginId, password, name, age, Role.USER);
    }

    public static User createAdminUser(String loginId, String password, String name, Integer age) {
        return new User(loginId, password, name, age, Role.ADMIN);
    }

    public void updateInfo(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}