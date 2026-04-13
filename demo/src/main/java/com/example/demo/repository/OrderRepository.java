package com.example.demo.repository;
import com.example.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>{
    @Query("select o from Order o join fetch o.user")
    List<Order> findAllWithUser();
}