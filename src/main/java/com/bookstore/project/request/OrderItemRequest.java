package com.bookstore.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    private Long bookId;
    private int quantity;
}
