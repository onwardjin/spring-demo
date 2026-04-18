package com.example.userorder.controller;

import com.example.userorder.dto.OrderRequestDto;
import com.example.userorder.dto.OrderResponseDto;
import com.example.userorder.entity.User;
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
            @AuthenticationPrincipal User user,
            @Valid @RequestBody OrderRequestDto request
            ){
        return orderService.createOrder(user.getId(), request);
    }

    @GetMapping("/me")
    public List<OrderResponseDto> getOrders(@AuthenticationPrincipal User user){
        return orderService.getOrdersByUserId(user.getId());
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrder(@AuthenticationPrincipal User user, @PathVariable Long id){
        return orderService.getOrder(user.getId(), id);
    }

    @PutMapping("/{id}")
    public OrderResponseDto update(@AuthenticationPrincipal User user, @PathVariable Long id, @Valid @RequestBody OrderRequestDto request){
        return orderService.updateOrder(user.getId(), id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@AuthenticationPrincipal User user, @PathVariable Long id){
        orderService.deleteOrder(user.getId(), id);
    }
}