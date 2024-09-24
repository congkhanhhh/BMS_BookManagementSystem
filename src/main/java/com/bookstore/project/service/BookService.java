package com.bookstore.project.service;

import com.bookstore.project.dto.BookDTO;
import com.bookstore.project.entity.Book;
import com.bookstore.project.entity.Genre;
import com.bookstore.project.entity.User;
import com.bookstore.project.repository.BookRepository;
import com.bookstore.project.repository.GenreRepository;
import com.bookstore.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenreRepository genreRepository;

    // Create or Update a book
    public BookDTO saveBook(BookDTO bookDTO) {
        // Map BookDTO to Book entity
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setDescription(bookDTO.getDescription());
        book.setPicture(bookDTO.getPicture());
        book.setPrice(bookDTO.getPrice());

        // Map Genre
        Optional<Genre> genre = genreRepository.findById(bookDTO.getGenreId());
        if (genre.isPresent()) {
            book.setGenre(genre.get());
        } else {
            throw new RuntimeException("Genre not found");
        }

        // Map User
        Optional<User> user = userRepository.findById((long) bookDTO.getUserId());
        if (user.isPresent()) {
            book.setUser(user.get());
        } else {
            throw new RuntimeException("User not found");
        }

        book.setCreated_at(bookDTO.getCreatedAt());

        // Save the book entity
        Book savedBook = bookRepository.save(book);

        // Convert back to BookDTO and return
        return mapToDTO(savedBook);
    }

    // Get a book by ID
    public Optional<BookDTO> getBookById(int id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(this::mapToDTO);
    }

    // Find books by userId
    public List<BookDTO> getBooksByUserId(int userId) {
        List<Book> books = bookRepository.findByUserId(userId);
        return books.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Get all books
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Delete a book by ID
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    // Utility to convert Book to BookDTO
    private BookDTO mapToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setPicture(book.getPicture());
        bookDTO.setPrice(book.getPrice());
        bookDTO.setGenreName(book.getGenre().getName());
        bookDTO.setUserId(Math.toIntExact(book.getUser().getId()));
        bookDTO.setCreatedAt(book.getCreated_at());
        return bookDTO;
    }

    // Get book titles by genreId
    public List<String> getBookTitlesByGenreId(int genreId) {
        List<Book> books = bookRepository.findByGenreId(genreId);
        return books.stream().map(Book::getTitle).collect(Collectors.toList());
    }
}
