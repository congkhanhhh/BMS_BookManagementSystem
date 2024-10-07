package com.bookstore.project.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RevenueByCategoryResponse {
    private String category;
    private BigDecimal totalRevenue;
}

