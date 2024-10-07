package com.bookstore.project.controller;

import com.bookstore.project.aspect.HasPermission;
import com.bookstore.project.enumerated.Permission;
import com.bookstore.project.request.OrderRequest;
import com.bookstore.project.responses.OrderResponse;
import com.bookstore.project.responses.RevenueByCategoryResponse;
import com.bookstore.project.responses.RevenueResponse;
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
    @Operation(summary = "Get list orders by id", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        OrderResponse orderResponse = orderServiceImpl.getOrderById(orderId); // Returns OrderResponse
        return ResponseEntity.ok(orderResponse);
    }

    // Search orders by username
    @GetMapping("/search")
    @HasPermission(Permission.SEARCH_ORDER)
    @Operation(summary = "Search Orders", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<List<OrderResponse>> searchOrders(@RequestParam String Username) {
        List<OrderResponse> orders = orderServiceImpl.searchOrders(Username); // Returns List of OrderResponse
        return ResponseEntity.ok(orders);
    }

    // Edit order by ID
    @PutMapping("/{orderId}")
    @HasPermission(Permission.EDIT_ORDER)
    @Operation(summary = "Edit orders", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<OrderResponse> editOrder(@PathVariable Long orderId, @RequestBody OrderRequest orderRequest) {
        OrderResponse updatedOrder = orderServiceImpl.editOrder(orderId, orderRequest); // Update and return the updated order
        return ResponseEntity.ok(updatedOrder);
    }

    // Delete order by ID
    @DeleteMapping("/{orderId}")
    @HasPermission(Permission.DELETE_ORDER)
    @Operation(summary = "Delete orders", security = {@SecurityRequirement(name = "bearerAuth")})
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

    @GetMapping("/revenue/daily")
    @HasPermission(Permission.GET_REVENUE_DAILY)
    @Operation(summary = "Get money daily", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<List<RevenueResponse>> getDailyRevenue() {
        List<RevenueResponse> dailyRevenue = orderServiceImpl.calculateDailyRevenue();
        return ResponseEntity.ok(dailyRevenue);
    }

    @GetMapping("/revenue/category")
    @HasPermission(Permission.GET_REVENUE_DAILY)
    @Operation(summary = "Get money category", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<List<RevenueByCategoryResponse>> getRevenueByCategory() {
        List<RevenueByCategoryResponse> revenueByCategory = orderServiceImpl.calculateRevenueByCategory();
        return ResponseEntity.ok(revenueByCategory);
    }

}
