package com.example.userorder.controller;

import com.example.userorder.dto.OrderRequestDto;
import com.example.userorder.dto.OrderResponseDto;
import com.example.userorder.security.CustomUserPrincipal;
import com.example.userorder.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponseDto create(
            @AuthenticationPrincipal CustomUserPrincipal user,
            @Valid @RequestBody OrderRequestDto request
            ){
        return orderService.createOrder(user.getId(), request);
    }

    @GetMapping("/me")
    public List<OrderResponseDto> getOrders(@AuthenticationPrincipal CustomUserPrincipal user){
        return orderService.getOrdersByUserId(user.getId());
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrder(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal user){
        return orderService.getOrder(id, user.getId());
    }

    @PutMapping("/{id}")
    public OrderResponseDto update(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal user, @Valid @RequestBody OrderRequestDto request){
        return orderService.updateOrder(id, user.getId(), request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal CustomUserPrincipal user){
        orderService.deleteOrder(id, user.getId());
    }
}