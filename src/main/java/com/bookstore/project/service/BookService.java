package com.bookstore.project.service;

import com.bookstore.project.entity.Book;
import com.bookstore.project.request.BookRequest;
import com.bookstore.project.responses.BookResponse;

import java.util.List;

public interface BookService {
    BookResponse createBook(BookRequest bookRequest);
    List<Book> getAllBooks();
    BookResponse getBookById(long id);
    List<BookResponse> getBooksByUserId(long userId);
    BookResponse updateBook(long id, BookRequest bookRequest);
    void deleteBook(long id);
    List<BookResponse> getBooksByGenreId(int genreId);
}

