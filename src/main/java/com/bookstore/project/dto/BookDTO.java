package com.bookstore.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
public class BookDTO {

    private int id;
    private String title;
    private String author;
    private String description;
    private String picture;
    private Double price;
    private int genreId;
    private String genreName;
    private int userId;
    private LocalDate createdAt;
}

