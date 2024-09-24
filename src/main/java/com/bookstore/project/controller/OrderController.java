package com.bookstore.project.controller;

import com.bookstore.project.entity.Order;
import com.bookstore.project.request.OrderRequest;
import com.bookstore.project.responses.OrderResponse;
import com.bookstore.project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orderBook")
    public ResponseEntity<OrderResponse> orderBook(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    // Search orders by customer name
    @GetMapping("/search")
    public ResponseEntity<List<Order>> searchOrders(@RequestParam String customerName) {
        List<Order> orders = orderService.searchOrders(customerName);
        return ResponseEntity.ok(orders);
    }
}
