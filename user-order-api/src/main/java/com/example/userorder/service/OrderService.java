package com.example.userorder.service;

import com.example.userorder.common.exception.OrderNotFoundException;
import com.example.userorder.common.exception.ProductNotFoundException;
import com.example.userorder.common.exception.UserNotFoundException;
import com.example.userorder.domain.order.Order;
import com.example.userorder.domain.order.vo.OrderQuantity;
import com.example.userorder.domain.product.Product;
import com.example.userorder.domain.user.User;
import com.example.userorder.dto.order.OrderItemCreateRequest;
import com.example.userorder.dto.order.OrderItemResponse;
import com.example.userorder.dto.order.OrderResponse;
import com.example.userorder.dto.order.OrderSearchCondition;
import com.example.userorder.repository.OrderItemRepository;
import com.example.userorder.repository.OrderRepository;
import com.example.userorder.repository.ProductRepository;
import com.example.userorder.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Long createOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Order order = Order.createOrder(user);
        return orderRepository.save(order).getId();
    }

    public Slice<OrderResponse> searchOrders(Long userId, OrderSearchCondition condition, Pageable pageable) {
        return orderRepository.searchOrders(userId, condition, pageable)
                .map(OrderResponse::from);
    }

    public List<OrderItemResponse> getOrderItems(Long userId, Long orderId) {
        if (!orderRepository.existsByUser_IdAndId(userId, orderId)) {
            throw new OrderNotFoundException();
        }
        return orderItemRepository.findWithProductByOrderId(orderId)
                .stream()
                .map(OrderItemResponse::from)
                .toList();
    }

    @Transactional
    public void addOrderItem(Long userId, Long orderId, OrderItemCreateRequest request) {
        Order order = orderRepository.findByUser_IdAndId(userId, orderId)
                .orElseThrow(OrderNotFoundException::new);
        Product product = productRepository.findById(request.productId())
                .orElseThrow(ProductNotFoundException::new);
        OrderQuantity orderQuantity = OrderQuantity.of(request.quantity());

        order.addOrderItem(product, orderQuantity);
    }

    @Transactional
    public void deleteOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByUser_IdAndId(userId, orderId)
                .orElseThrow(OrderNotFoundException::new);

        orderRepository.delete(order);
    }
}