package com.example.userorder.repository;

import com.example.userorder.domain.order.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    Slice<Order> findByUser_Id(Long userId, Pageable pageable);

    boolean existsByUser_IdAndId(Long userId, Long orderId);

    Optional<Order> findByUser_IdAndId(Long userId, Long orderId);
}