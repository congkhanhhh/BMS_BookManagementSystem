package com.bookstore.project.service.impl;

import com.bookstore.project.entity.Book;
import com.bookstore.project.entity.Favorite;
import com.bookstore.project.entity.User;
import com.bookstore.project.exception.ResourceNotFoundException;
import com.bookstore.project.repository.FavoriteRepository;
import com.bookstore.project.repository.UserRepository;
import com.bookstore.project.responses.BookResponse;
import com.bookstore.project.repository.BookRepository;
import com.bookstore.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public void addFavorite(long userId, long bookId) {
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

    public void removeFavorite(long userId, long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        Favorite favorite = favoriteRepository.findByUserAndBook(user, book)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite entry not found"));

        favoriteRepository.delete(favorite);
    }

    public List<BookResponse> getFavorites(long userId) {
        // Fetch the user by ID and handle if the user is not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Fetch the user's favorite books and map them to BookResponse
        return favoriteRepository.findByUser(user).stream()
                .map(favorite -> {
                    Book book = favorite.getBook();

                    // Map Book entity to BookResponse
                    BookResponse response = new BookResponse();
                    response.setId(book.getId());
                    response.setTitle(book.getTitle());
                    response.setAuthor(book.getAuthor());
                    response.setDescription(book.getDescription());
                    response.setImageUrl(book.getImageUrl()); // Set picture if available
                    response.setPrice(book.getPrice());     // Set price if available
                    response.setGenreName(book.getGenre().getName()); // Set genre name
                    response.setUsername(user.getUsername()); // Set user’s username
                    response.setCreatedAt(book.getCreated_at()); // Set created date
                    return response;
                })
                .collect(Collectors.toList());
    }
}
