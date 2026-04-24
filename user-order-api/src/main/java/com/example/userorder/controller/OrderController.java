package com.example.userorder.controller;

import com.example.userorder.dto.OrderCreateRequestDto;
import com.example.userorder.dto.OrderResponseDto;
import com.example.userorder.dto.OrderUpdateRequestDto;
import com.example.userorder.security.CustomUserPrincipal;
import com.example.userorder.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponseDto createOrder(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody OrderCreateRequestDto request
    ) {
        return orderService.createOrder(principal.getId(), request);
    }

    @GetMapping
    public List<OrderResponseDto> getAllOrders(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return orderService.getAllOrdersByUserId(principal.getId());
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrder(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long orderId
    ) {
        return orderService.getOrderById(principal.getId(), orderId);
    }

    @PatchMapping("/{id}")
    public OrderResponseDto updateOrder(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @RequestBody OrderUpdateRequestDto request
    ) {
        return orderService.updateOrder(principal.getId(), orderId, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long orderId
    ) {
        orderService.deleteOrder(principal.getId(), orderId);
        return ResponseEntity.noContent().build();
    }
}