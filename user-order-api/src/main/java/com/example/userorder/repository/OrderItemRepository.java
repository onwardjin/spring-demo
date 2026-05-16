package com.example.userorder.repository;

import com.example.userorder.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_Id(Long orderId);
    
    @Query("SELECT oi from OrderItem oi join fetch oi.product where oi.order.id = :orderId")
    List<OrderItem> findWithProductByOrderId(Long orderId);
}