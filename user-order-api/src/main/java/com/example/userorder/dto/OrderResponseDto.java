package com.example.userorder.dto;

import com.example.userorder.entity.Order;
import com.example.userorder.entity.User;

public class OrderResponseDto {
    private Long id;
    private String item;
    private Long userId;
    private String userName;

    public OrderResponseDto(Order order){
        this.id = order.getId();
        this.item = order.getItem();
        User user = order.getUser();
        this.userId = user.getId();
        this.userName = user.getName();
    }

    public Long getId(){
        return id;
    }
    public String getItem(){
        return item;
    }
    public Long getUserId(){
        return userId;
    }
    public String getUserName(){
        return userName;
    }
}