package com.bookstore.project.service;

import com.bookstore.project.dto.BookDTO;
import com.bookstore.project.entity.Book;
import com.bookstore.project.entity.Genre;
import com.bookstore.project.repository.BookRepository;
import com.bookstore.project.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    public List<BookDTO> getBooksByGenre(int genreId) {
        Genre genre = genreRepository.findById(genreId).orElse(null);
        if (genre != null) {
            return bookRepository.findByGenre(genre).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<BookDTO> getBookById(int id) {
        return bookRepository.findById(id).map(this::convertToDTO);
    }

    public BookDTO saveBook(BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setDescription(book.getDescription());

        if (book.getGenre() != null) {
            dto.setGenreId(book.getGenre().getId());
        }
        dto.setGenreName((book.getGenre().getName()));
        if (book.getCreated_at() != null) {
            dto.setCreated_at(book.getCreated_at());
        }

        return dto;
    }

    private Book convertToEntity(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setDescription(dto.getDescription());

        if (dto.getCreated_at() != null) {
            book.setCreated_at(dto.getCreated_at());
        } else {
            book.setCreated_at(null);
        }

        // Check if genreId is not 0 and find the Genre by its id
        if (dto.getGenreId() != 0) {
            Genre genre = genreRepository.findById(dto.getGenreId())
                    .orElseThrow(() -> new IllegalArgumentException("Genre with id " + dto.getGenreId() + " not found"));
            book.setGenre(genre);
        } else {
            book.setGenre(null); // Handle case where genre is not set
        }

        return book;
    }
}
