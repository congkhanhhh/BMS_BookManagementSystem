package com.bookstore.project.service;

import com.bookstore.project.request.OrderRequest;
import com.bookstore.project.responses.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
    List<OrderResponse> getOrdersByUserId(Long userId);
    OrderResponse getOrderByIdAndUserId(Long orderId, Long userId);
    List<OrderResponse> searchOrdersByUserId(Long userId, String keyword);
    List<OrderResponse> getAllOrders();
    void deleteOrder(Long orderId);
}


