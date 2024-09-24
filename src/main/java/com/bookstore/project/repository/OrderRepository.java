package com.bookstore.project.repository;

import com.bookstore.project.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserUsernameContainingIgnoreCase(String username);
}
