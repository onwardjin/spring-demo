package com.example.userorder.exception;

public class OrderNotFoundExeption extends RuntimeException {
    public OrderNotFoundExeption() {
        super("Order not found");
    }
}
