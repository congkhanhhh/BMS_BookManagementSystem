package com.bookstore.project.controller;

import com.bookstore.project.request.OrderRequest;
import com.bookstore.project.responses.OrderResponse;
import com.bookstore.project.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @PostMapping("/orderBook")
    public ResponseEntity<OrderResponse> orderBook(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderServiceImpl.createOrder(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }
    // Get all orders
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderServiceImpl.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        OrderResponse orderResponse = orderServiceImpl.getOrderById(orderId); // Returns OrderResponse
        return ResponseEntity.ok(orderResponse);
    }

    // Search orders by username
    @GetMapping("/search")
    public ResponseEntity<List<OrderResponse>> searchOrders(@RequestParam String Username) {
        List<OrderResponse> orders = orderServiceImpl.searchOrders(Username); // Returns List of OrderResponse
        return ResponseEntity.ok(orders);
    }
}
