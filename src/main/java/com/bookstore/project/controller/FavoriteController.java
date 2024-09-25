package com.bookstore.project.controller;

import com.bookstore.project.exception.ResourceNotFoundException;
import com.bookstore.project.responses.BookResponse;
import com.bookstore.project.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/{userId}/books/{bookId}")
    public ResponseEntity<Void> addFavorite(@PathVariable int userId, @PathVariable int bookId) {
        favoriteService.addFavorite(userId, bookId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}/books/{bookId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable int userId, @PathVariable int bookId) {
        try {
            favoriteService.removeFavorite(userId, bookId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<BookResponse>> getFavorites(@PathVariable int userId) {
        List<BookResponse> favorites = favoriteService.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

}

