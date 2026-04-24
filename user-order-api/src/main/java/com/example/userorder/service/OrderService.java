package com.example.userorder.service;

import com.example.userorder.dto.OrderCreateRequestDto;
import com.example.userorder.dto.OrderResponseDto;
import com.example.userorder.dto.OrderUpdateRequestDto;
import com.example.userorder.entity.Order;
import com.example.userorder.entity.User;
import com.example.userorder.exception.OrderNotFoundException;
import com.example.userorder.exception.UserNotFoundException;
import com.example.userorder.repository.OrderRepository;
import com.example.userorder.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderCreateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Order order = Order.createOrder(request.productName(), request.quantity(), request.price(), user);
        Order savedOrder = orderRepository.save(order);

        return new OrderResponseDto(savedOrder);
    }

    public List<OrderResponseDto> getAllOrdersByUserId(Long userId) {
        return orderRepository.findAllByUser_Id(userId).stream()
                .map(OrderResponseDto::new)
                .toList();
    }

    public OrderResponseDto getOrderById(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUser_Id(orderId, userId)
                .orElseThrow(OrderNotFoundException::new);
        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrder(Long userId, Long orderId, OrderUpdateRequestDto request) {
        Order order = orderRepository.findByIdAndUser_Id(orderId, userId)
                .orElseThrow(OrderNotFoundException::new);
        order.updateOrder(request);
        return new OrderResponseDto(order);
    }

    @Transactional
    public void deleteOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUser_Id(orderId, userId)
                .orElseThrow(OrderNotFoundException::new);
        orderRepository.delete(order);
    }
}