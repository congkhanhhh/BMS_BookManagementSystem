package com.bookstore.project.controller;

import com.bookstore.project.dto.BookDTO;
import com.bookstore.project.service.BookService;
import com.bookstore.project.service.UserService;
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

    @Autowired
    private UserService userService;


    // Get a book by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable int id) {
        Optional<BookDTO> book = bookService.getBookById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Book not found");
        }
    }
    // Create a new book
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        BookDTO savedBook = bookService.saveBook(bookDTO);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBooksByUserId(@PathVariable long userId) {
        if (!userService.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User with ID " + userId + " not found.");
        }

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
    public ResponseEntity<?> updateBook(@PathVariable int id, @RequestBody BookDTO bookDTO) {
        Optional<BookDTO> existingBook = bookService.getBookById(id);
        if (existingBook.isPresent()) {
            bookDTO.setId(id); // Ensure we are updating the correct book
            BookDTO updatedBook = bookService.saveBook(bookDTO);
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Book not found");
        }
    }

    // Delete a book by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable int id) {
        if (bookService.getBookById(id).isPresent()) {
            // Add condition for forbidden response, e.g., user lacks permissions
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Book not found");
        }
    }


    // Find books by genreId and return their titles
    @GetMapping("/genre/{genreId}/titles")
    public ResponseEntity<?> getBookTitlesByGenre(@PathVariable int genreId) {
        // Add condition for forbidden response if needed
        List<String> bookTitles = bookService.getBookTitlesByGenreId(genreId);
        return ResponseEntity.ok(bookTitles);
    }
}

