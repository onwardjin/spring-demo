package com.example.userorder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Order(){ }
    public Order(String item, User user){
        this.item = item;
        this.user = user;
    }

    public Long getId(){ return id; }
    public String getItem(){ return item; }
    public User getUser(){ return user; }

    public void setItem(String item){ this.item = item; }
}
