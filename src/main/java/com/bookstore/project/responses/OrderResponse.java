package com.bookstore.project.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long orderId;
    private String orderDate;
    private List<OrderItemResponse> orderItems;
    private BigDecimal totalPrice;
}
