package com.bookstore.project.service;

import com.bookstore.project.dto.BookDTO;
import com.bookstore.project.entity.Book;
import com.bookstore.project.entity.Favorite;
import com.bookstore.project.entity.User;
import com.bookstore.project.exception.ResourceNotFoundException;
import com.bookstore.project.repository.BookRepository;
import com.bookstore.project.repository.FavoriteRepository;
import com.bookstore.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public void addFavorite(long userId, int bookId) {
        User user = userRepository.findById( userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        if (favoriteRepository.existsByUserAndBook(user, book)) {
            throw new IllegalArgumentException("Book is already in favorites");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setBook(book);
        favoriteRepository.save(favorite);
    }

    public void removeFavorite(long userId, int bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        Favorite favorite = favoriteRepository.findByUserAndBook(user, book)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite entry not found"));

        favoriteRepository.delete(favorite);
    }

    public List<BookDTO> getFavorites(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return favoriteRepository.findByUser(user).stream()
                .map(favorite -> {
                    Book book = favorite.getBook();
                    // Tạo BookDTO từ entity Book
                    BookDTO dto = new BookDTO();
                    dto.setId(book.getId());
                    dto.setTitle(book.getTitle());
                    dto.setAuthor(book.getAuthor());
                    dto.setDescription(book.getDescription());
                    dto.setPicture(book.getPicture()); // Nếu có trường picture
                    dto.setPrice(book.getPrice());     // Nếu có trường price
                    dto.setGenreId(book.getGenre().getId());
                    dto.setGenreName(book.getGenre().getName()); // Lấy tên thể loại
                    dto.setUserId(Math.toIntExact(user.getId()));       // Lấy ID người dùng
                    dto.setCreatedAt(book.getCreated_at());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setDescription(book.getDescription());
        dto.setGenreName(book.getGenre() != null ? book.getGenre().getName() : null); // Nếu bạn có thông tin genre

        return dto;
    }
}
