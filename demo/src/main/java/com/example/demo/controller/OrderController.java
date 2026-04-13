package com.example.demo.controller;
import com.example.demo.service.OrderService;
import com.example.demo.dto.OrderRequestDto;
import com.example.demo.dto.OrderResponseDto;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponseDto createOrder(@Valid @RequestBody OrderRequestDto request){
        return orderService.createOrder(request);
    }

    @GetMapping
    public List<OrderResponseDto> getOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrder(@PathVariable Long id){
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}")
    public OrderResponseDto update(
            @Valid @PathVariable Long id,
            @RequestBody OrderRequestDto request
    ) {
        return orderService.updateOrder(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        orderService.deleteOrder(id);
    }
}