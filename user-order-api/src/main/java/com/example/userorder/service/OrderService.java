package com.example.userorder.service;

import com.example.userorder.dto.OrderRequestDto;
import com.example.userorder.dto.OrderResponseDto;
import com.example.userorder.entity.Order;
import com.example.userorder.entity.User;
import com.example.userorder.exception.OrderNotFoundException;
import com.example.userorder.repository.OrderRepository;
import com.example.userorder.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository){
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto){
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(OrderNotFoundException::new);
        Order order = new Order(requestDto.getName(), user);

        return new OrderResponseDto(orderRepository.save(order));
    }

    public List<OrderResponseDto> readAllOrders(){
        return orderRepository.findAllWithUser().stream()
                .map(OrderResponseDto::new)
                .toList();
    }

    public OrderResponseDto readOrder(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrder(Long id, OrderRequestDto requestDto){
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        order.setItem(requestDto.getName());

        return new OrderResponseDto(order);
    }

    public void deleteOrder(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        orderRepository.delete(order);
    }
}