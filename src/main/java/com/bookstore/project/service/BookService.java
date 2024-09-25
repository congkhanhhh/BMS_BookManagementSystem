package com.bookstore.project.service;

import com.bookstore.project.entity.Book;
import com.bookstore.project.entity.Genre;
import com.bookstore.project.entity.User;
import com.bookstore.project.repository.BookRepository;
import com.bookstore.project.repository.GenreRepository;
import com.bookstore.project.repository.UserRepository;
import com.bookstore.project.request.BookRequest;
import com.bookstore.project.responses.BookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
    public class BookService {

        @Autowired
        private BookRepository bookRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private GenreRepository genreRepository;

        // Create a new book
        public BookResponse createBook(BookRequest bookRequest) {
            Book book = new Book();
            book.setTitle(bookRequest.getTitle());
            book.setAuthor(bookRequest.getAuthor());
            book.setDescription(bookRequest.getDescription());
            book.setPicture(bookRequest.getPicture());
            book.setPrice(bookRequest.getPrice());

            // Fetch Genre and User
            Genre genre = genreRepository.findById(bookRequest.getGenreId())
                    .orElseThrow(() -> new RuntimeException("Genre not found"));
            User user = userRepository.findById(bookRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            book.setGenre(genre);
            book.setUser(user);
            book.setCreated_at(LocalDate.now());

            Book savedBook = bookRepository.save(book);
            return mapToBookResponse(savedBook);
        }

        // Get all books
        public List<BookResponse> getAllBooks() {
            List<Book> books = bookRepository.findAll();
            return books.stream().map(this::mapToBookResponse).collect(Collectors.toList());
        }

        // Get a book by ID
        public BookResponse getBookById(long id) {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            return mapToBookResponse(book);
        }

        // Get books by user ID
        public List<BookResponse> getBooksByUserId(long userId) {
            List<Book> books = bookRepository.findByUserId(userId);
            return books.stream().map(this::mapToBookResponse).collect(Collectors.toList());
        }

        // Update a book
        public BookResponse updateBook(long id, BookRequest bookRequest) {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            book.setTitle(bookRequest.getTitle());
            book.setAuthor(bookRequest.getAuthor());
            book.setDescription(bookRequest.getDescription());
            book.setPicture(bookRequest.getPicture());
            book.setPrice(bookRequest.getPrice());

            // Fetch Genre and User if necessary
            Genre genre = genreRepository.findById(bookRequest.getGenreId())
                    .orElseThrow(() -> new RuntimeException("Genre not found"));
            User user = userRepository.findById(bookRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            book.setGenre(genre);
            book.setUser(user);

            Book updatedBook = bookRepository.save(book);
            return mapToBookResponse(updatedBook);
        }

        // Delete a book by ID
        public void deleteBook(long id) {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            bookRepository.delete(book);
        }
        // Get books by genre ID
        public List<BookResponse> getBooksByGenreId(int genreId) {
            List<Book> books = bookRepository.findByGenreId(genreId);
            return books.stream().map(this::mapToBookResponse).collect(Collectors.toList());
        }

        // Utility method to map Book entity to BookResponse DTO
        private BookResponse mapToBookResponse(Book book) {
            BookResponse response = new BookResponse();
            response.setId(book.getId());
            response.setTitle(book.getTitle());
            response.setAuthor(book.getAuthor());
            response.setDescription(book.getDescription());
            response.setPicture(book.getPicture());
            response.setPrice(book.getPrice());
            response.setGenreName(book.getGenre().getName());
            response.setUsername(book.getUser().getUsername());
            response.setCreatedAt(book.getCreated_at());
            return response;
        }

    }
