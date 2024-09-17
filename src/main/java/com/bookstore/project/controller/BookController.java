package com.bookstore.project.controller;

import com.bookstore.project.dto.BookDTO;
import com.bookstore.project.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Create a new book
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        BookDTO savedBook = bookService.saveBook(bookDTO);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookDTO>> getBooksByUserId(@PathVariable int userId) {
        List<BookDTO> books = bookService.getBooksByUserId(userId);
        return ResponseEntity.ok(books);
    }

    // Get all books
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // Update a book
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable int id, @RequestBody BookDTO bookDTO) {
        Optional<BookDTO> existingBook = bookService.getBookById(id);
        if (existingBook.isPresent()) {
            bookDTO.setId(id); // Ensure we are updating the correct book
            BookDTO updatedBook = bookService.saveBook(bookDTO);
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a book by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        if (bookService.getBookById(id).isPresent()) {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Find books by genreId and return their titles
    @GetMapping("/genre/{genreId}/titles")
    public ResponseEntity<List<String>> getBookTitlesByGenre(@PathVariable int genreId) {
        List<String> bookTitles = bookService.getBookTitlesByGenreId(genreId);
        return ResponseEntity.ok(bookTitles);
    }
}

