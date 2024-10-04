package com.bookstore.project.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BookResponse {
    private long id;
    private String title;
    private String author;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private String genreName; // You can show the genre name instead of ID
    private String username;  // Display the user who added the book
    private LocalDate createdAt;
}
