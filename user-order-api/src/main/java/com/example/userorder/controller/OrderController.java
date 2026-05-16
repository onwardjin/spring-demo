package com.example.userorder.controller;

import com.example.userorder.dto.order.OrderItemCreateRequest;
import com.example.userorder.dto.order.OrderItemResponse;
import com.example.userorder.dto.order.OrderResponse;
import com.example.userorder.dto.order.OrderSearchCondition;
import com.example.userorder.repository.OrderRepository;
import com.example.userorder.security.CustomUserPrincipal;
import com.example.userorder.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
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
    public Long createOrder(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return orderService.createOrder(principal.getId());
    }

    @PostMapping("/{orderId}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addOrderItem(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemCreateRequest request
    ) {
        orderService.addOrderItem(principal.getId(), orderId, request);
    }

    @GetMapping
    public Slice<OrderResponse> searchOrders(
            @AuthenticationPrincipal CustomUserPrincipal principal, OrderSearchCondition condition, Pageable pageable
    ) {
        return orderService.searchOrders(principal.getId(), condition, pageable);
    }

    @GetMapping("/{orderId}")
    public List<OrderItemResponse> getOrderItems(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long orderId
    ) {
        return orderService.getOrderItems(principal.getId(), orderId);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long orderId
    ) {
        orderService.deleteOrder(principal.getId(), orderId);
    }
}