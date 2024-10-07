package com.bookstore.project.repository;

import com.bookstore.project.entity.Order;
import com.bookstore.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserUsernameContainingIgnoreCase(String username);
    List<Order> findByUser(User user);
    @Query("SELECT DATE(o.orderDate), SUM(o.totalPrice) FROM Order o GROUP BY DATE(o.orderDate)")
    List<Object[]> calculateTotalRevenueByDay();
    @Query("SELECT b.genre.name, SUM(oi.price) FROM OrderItem oi " +
                "JOIN oi.book b " +
                "GROUP BY b.genre.name")
    List<Object[]> calculateTotalRevenueByCategory();
}
