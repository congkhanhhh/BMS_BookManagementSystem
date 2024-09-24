package com.bookstore.project.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long orderId;
    private Date orderDate;
    private BigDecimal totalPrice;
    private List<OrderItemResponse> orderItems;
}
