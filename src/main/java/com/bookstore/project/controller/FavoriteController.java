package com.bookstore.project.controller;

import com.bookstore.project.aspect.HasPermission;
import com.bookstore.project.enumerated.Permission;
import com.bookstore.project.exception.ResourceNotFoundException;
import com.bookstore.project.responses.BookResponse;
import com.bookstore.project.service.impl.FavoriteServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/favorites")
@Tag(name = "[ADMIN] Favorites Management", description = "Favorites APIs")
public class FavoriteController {

    @Autowired
    private FavoriteServiceImpl favoriteServiceImpl;

    @PostMapping("/{userId}/books/{bookId}")
    @HasPermission(Permission.ADD_FAVORITE_BOOK)
    public ResponseEntity<Void> addFavorite(@PathVariable int userId, @PathVariable int bookId) {
        favoriteServiceImpl.addFavorite(userId, bookId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}/books/{bookId}")
    @HasPermission(Permission.DELETE_FAVORITE_BOOK)
    public ResponseEntity<Void> removeFavorite(@PathVariable int userId, @PathVariable int bookId) {
        try {
            favoriteServiceImpl.removeFavorite(userId, bookId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{userId}")
    @HasPermission(Permission.VIEW_USER_FAVORITE_BOOK)
    public ResponseEntity<List<BookResponse>> getFavorites(@PathVariable int userId) {
        List<BookResponse> favorites = favoriteServiceImpl.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

}

