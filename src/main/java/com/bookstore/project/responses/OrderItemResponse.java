package com.bookstore.project.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponse {
    private Long bookId;
    private String bookTitle;
    private int quantity;
    private BigDecimal price;
}
