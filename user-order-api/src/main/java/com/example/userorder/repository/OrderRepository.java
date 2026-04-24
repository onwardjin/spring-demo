package com.example.userorder.repository;

import com.example.userorder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser_Id(Long userId);

    Optional<Order> findByIdAndUser_Id(Long orderId, Long userId);
}