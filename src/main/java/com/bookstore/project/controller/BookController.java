package com.bookstore.project.controller;


import com.bookstore.project.request.BookRequest;
import com.bookstore.project.responses.BookResponse;
import com.bookstore.project.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Create a new book
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest bookRequest) {
        BookResponse bookResponse = bookService.createBook(bookRequest);
        return ResponseEntity.ok(bookResponse);
    }

    // Get all books
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // Get a book by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable int id) {
        BookResponse bookResponse = bookService.getBookById(id);
        return ResponseEntity.ok(bookResponse);
    }

    // Get books by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookResponse>> getBooksByUserId(@PathVariable int userId) {
        List<BookResponse> books = bookService.getBooksByUserId(userId);
        return ResponseEntity.ok(books);
    }

    // Update a book
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable int id, @RequestBody BookRequest bookRequest) {
        BookResponse bookResponse = bookService.updateBook(id, bookRequest);
        return ResponseEntity.ok(bookResponse);
    }

    // Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    // Get books by genre ID
    @GetMapping("/genre/{genreId}")
    public ResponseEntity<List<BookResponse>> getBooksByGenreId(@PathVariable int genreId) {
        List<BookResponse> books = bookService.getBooksByGenreId(genreId);
        return ResponseEntity.ok(books);
    }
}

