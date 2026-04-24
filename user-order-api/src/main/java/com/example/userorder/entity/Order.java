package com.example.userorder.entity;

import com.example.userorder.dto.OrderUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private Integer quantity;
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Order(String productName, Integer quantity, Integer price, User user) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.user = user;
    }

    public static Order createOrder(String productName, Integer quantity, Integer price, User user) {
        return new Order(productName, quantity, price, user);
    }

    public void updateOrder(OrderUpdateRequestDto request) {
        this.productName = request.productName();
        this.quantity = request.quantity();
        this.price = request.price();
    }
}