package com.bookstore.project.controller;


import com.bookstore.project.aspect.HasPermission;
import com.bookstore.project.enumerated.Permission;
import com.bookstore.project.request.BookRequest;
import com.bookstore.project.responses.BookResponse;
import com.bookstore.project.service.impl.BookServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/admin/books")
@Tag(name = "[ADMIN] Books Management", description = "Book APIs")
public class BookController {

    @Autowired
    private BookServiceImpl bookService;

    // Create a new book
    @PostMapping
    @HasPermission(Permission.ADD_BOOK)
    @Operation(summary = "Add new books", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest bookRequest) {
        BookResponse bookResponse = bookService.createBook(bookRequest);
        return ResponseEntity.ok(bookResponse);
    }

    // Get all books
    @GetMapping
    @HasPermission(Permission.VIEW_ALL_BOOK)
    @Operation(summary = "Get list books", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // Get a book by ID
    @GetMapping("/{id}")
    @HasPermission(Permission.VIEW_BOOK_BY_ID)
    public ResponseEntity<BookResponse> getBookById(@PathVariable int id) {
        BookResponse bookResponse = bookService.getBookById(id);
        return ResponseEntity.ok(bookResponse);
    }

    // Get books by user ID
    @GetMapping("/user/{userId}")
    @HasPermission(Permission.VIEW_USER_BOOK)
    public ResponseEntity<List<BookResponse>> getBooksByUserId(@PathVariable int userId) {
        List<BookResponse> books = bookService.getBooksByUserId(userId);
        return ResponseEntity.ok(books);
    }

    // Update a book
    @PutMapping("/{id}")
    @HasPermission(Permission.EDIT_BOOK)
    public ResponseEntity<BookResponse> updateBook(@PathVariable int id, @RequestBody BookRequest bookRequest) {
        BookResponse bookResponse = bookService.updateBook(id, bookRequest);
        return ResponseEntity.ok(bookResponse);
    }

    // Delete a book
    @DeleteMapping("/{id}")
    @HasPermission(Permission.DELETE_BOOK)
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    // Get books by genre ID
    @GetMapping("/genre/{genreId}")
    @HasPermission(Permission.VIEW_ALL_BOOK_BY_GENRE)
    public ResponseEntity<List<BookResponse>> getBooksByGenreId(@PathVariable int genreId) {
        List<BookResponse> books = bookService.getBooksByGenreId(genreId);
        return ResponseEntity.ok(books);
    }
}

