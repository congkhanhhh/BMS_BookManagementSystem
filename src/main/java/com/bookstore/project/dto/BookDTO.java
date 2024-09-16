package com.bookstore.project.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class BookDTO {
    private int id;
    private String title;
    private String author;
    private String description;
    private int genreId;
    private String genreName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date created_at;

    public BookDTO() {
    }

    public BookDTO(int id, String title, String author, String description, int genreId,String genreName, Date created_at) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.genreId = genreId;
        this.genreName = genreName;
        this.created_at = created_at;
    }
}
