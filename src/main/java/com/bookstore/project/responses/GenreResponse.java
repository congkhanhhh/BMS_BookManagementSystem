package com.bookstore.project.responses;

import com.bookstore.project.entity.Genre;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreResponse {
    private int id;
    private  String name;
    public static GenreResponse mapToGenreResponse(Genre genre){
        GenreResponse genreResponse = new GenreResponse();
        genreResponse.setName(genre.getName());
        return genreResponse;
    }
}
