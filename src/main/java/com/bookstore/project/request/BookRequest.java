package com.bookstore.project.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookRequest {
    @NotNull
    private String title;

    @NotNull
    private String author;

    private String description;

    private String picture;

    @NotNull
    private BigDecimal price;

    @NotNull
    private long genreId;

    @NotNull
    private long userId;
}
