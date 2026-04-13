package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.OrderRequestDto;
import com.example.demo.dto.OrderResponseDto;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.UserNotFoundException;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderService(
            UserRepository userRepository,
            OrderRepository orderRepository
    ) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(UserNotFoundException::new);
        Order order = new Order(request.getItem(), user);

        return new OrderResponseDto(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders(){
        return orderRepository.findAllWithUser().stream()
                .map(OrderResponseDto::new)
                .toList();
    }

    public OrderResponseDto getOrderById(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrder(Long id, OrderRequestDto request){
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(UserNotFoundException::new);

        order.setItem(request.getItem());
        order.setUser(user);

        return new OrderResponseDto(order);
    }

    @Transactional
    public void deleteOrder(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        orderRepository.delete(order);
    }
}