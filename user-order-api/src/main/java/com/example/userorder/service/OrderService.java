package com.example.userorder.service;

import com.example.userorder.dto.OrderRequestDto;
import com.example.userorder.dto.OrderResponseDto;
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

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrder(Long id, OrderRequestDto request){
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        order.setItem(request.getItem());

        return new OrderResponseDto(order);
    }

    @Transactional
    public void deleteOrder(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        orderRepository.delete(order);
    }
}