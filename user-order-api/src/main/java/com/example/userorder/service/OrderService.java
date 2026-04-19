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
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository){
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderRequestDto request){
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Order order = new Order(request.getItem(), user);
        orderRepository.save(order);

        return new OrderResponseDto(order);
    }

    public List<OrderResponseDto> getOrdersByUserId(Long userId){
        // 유저 유효확인 생략, 추후 추가 예정
        List<OrderResponseDto> orders = orderRepository.findAllWithUserId(userId)
                .stream()
                .map(OrderResponseDto::new)
                .toList();

        return orders;
    }

    public OrderResponseDto getOrder(Long orderId, Long userId){
        Order order = getMyOrder(orderId, userId);
        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrder(Long orderId, Long userId, OrderRequestDto request){
        Order order = getMyOrder(orderId, userId);
        order.setItem(request.getItem());

        return new OrderResponseDto(order);
    }

    @Transactional
    public void deleteOrder(Long orderId, Long userId){
        Order order = getMyOrder(orderId, userId);
        orderRepository.delete(order);
    }

    private Order getMyOrder(Long orderId, Long userId){
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(OrderNotFoundException::new);
    }
}