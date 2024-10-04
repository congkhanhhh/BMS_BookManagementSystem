package com.bookstore.project.controller;

import com.bookstore.project.aspect.HasPermission;
import com.bookstore.project.enumerated.Permission;
import com.bookstore.project.request.OrderRequest;
import com.bookstore.project.responses.OrderResponse;
import com.bookstore.project.service.impl.OrderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/orders")
@Tag(name = "[ADMIN] Orders Management", description = "Order APIs")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    // Create orders
    @PostMapping("/orderBook")
    @HasPermission(Permission.CREATE_ORDER)
    @Operation(summary = "Create new orders", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<OrderResponse> orderBook(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderServiceImpl.createOrder(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }
    // Get all orders
    @GetMapping
    @HasPermission(Permission.VIEW_ORDER)
    @Operation(summary = "Get list orders", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderServiceImpl.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get order by ID
    @GetMapping("/{orderId}")
    @HasPermission(Permission.VIEW_ORDER_DETAILS)
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        OrderResponse orderResponse = orderServiceImpl.getOrderById(orderId); // Returns OrderResponse
        return ResponseEntity.ok(orderResponse);
    }

    // Search orders by username
    @GetMapping("/search")
    @HasPermission(Permission.SEARCH_ORDER)
    public ResponseEntity<List<OrderResponse>> searchOrders(@RequestParam String Username) {
        List<OrderResponse> orders = orderServiceImpl.searchOrders(Username); // Returns List of OrderResponse
        return ResponseEntity.ok(orders);
    }

    // Edit order by ID
    @PutMapping("/{orderId}")
    @HasPermission(Permission.EDIT_ORDER)
    public ResponseEntity<OrderResponse> editOrder(@PathVariable Long orderId, @RequestBody OrderRequest orderRequest) {
        OrderResponse updatedOrder = orderServiceImpl.editOrder(orderId, orderRequest); // Update and return the updated order
        return ResponseEntity.ok(updatedOrder);
    }

    // Delete order by ID
    @DeleteMapping("/{orderId}")
    @HasPermission(Permission.DELETE_ORDER)
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderServiceImpl.deleteOrder(orderId); // Delete order
        return ResponseEntity.noContent().build(); // Return 204 No Content if successful
    }

    @GetMapping("/me")
    @HasPermission(Permission.VIEW_OWN_ORDERS)
    @Operation(summary = "Get list my orders", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<List<OrderResponse>> getOwnOrders() {
        List<OrderResponse> orders = orderServiceImpl.getOwnOrders();
        return ResponseEntity.ok(orders);
    }

}
