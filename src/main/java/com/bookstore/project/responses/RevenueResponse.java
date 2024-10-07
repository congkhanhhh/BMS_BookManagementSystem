package com.bookstore.project.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class RevenueResponse {
    private Date date;
    private BigDecimal totalRevenue;
}

