package com.example.userorder.repository;

import com.example.userorder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o join fetch o.user where o.user.id = :userId")
    List<Order> findAllWithUserId(@Param("userId") Long userId);

    @Query("select o from Order o join fetch o.user where o.user.id = :userId and o.id = :orderId ")
    Optional<Order> findByUserIdAndOrderId(@Param("userId")Long userId, @Param("orderId")Long orderId);
}

