package com.example.userorder.controller;

import com.example.userorder.dto.OrderRequestDto;
import com.example.userorder.dto.OrderResponseDto;
import com.example.userorder.service.OrderService;
import jakarta.validation.Valid;
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
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto requestDto){
        return orderService.createOrder(requestDto);
    }

    @GetMapping
    public List<OrderResponseDto> readAllOrders(){
        return orderService.readAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponseDto readOrder(@Valid @PathVariable Long id){
        return orderService.readOrder(id);
    }

    @PutMapping("/{id}")
    public OrderResponseDto updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequestDto requestDto){
        return orderService.updateOrder(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
    }
}