package com.bookstore.project.controller;

import com.bookstore.project.aspect.HasPermission;
import com.bookstore.project.entity.Genre;
import com.bookstore.project.enumerated.Permission;
import com.bookstore.project.service.impl.GenreServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/genres")
@Tag(name = "[ADMIN] Genres Management", description = "Genre APIs")
public class GenreController {

    @Autowired
    private GenreServiceImpl genreServiceImpl;

    @GetMapping
    @HasPermission(Permission.VIEW_ALL_GENRE)
    @Operation(summary = "Add new genre", security = {@SecurityRequirement(name = "bearerAuth")})
    public List<Genre> getAllGenres() {
        return genreServiceImpl.getAllGenres();
    }

    @PostMapping
    @HasPermission(Permission.ADD_GENRE)
    @Operation(summary = "Add new genre", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        Genre createdGenre = genreServiceImpl.saveGenre(genre);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGenre);
    }

    @PutMapping("/{id}")
    @HasPermission(Permission.EDIT_GENRE)
    @Operation(summary = "Edit genre", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<Genre> updateGenre(@PathVariable int id, @RequestBody Genre genre) {
        genre.setId(id);
        Genre updatedGenre = genreServiceImpl.saveGenre(genre);
        return ResponseEntity.ok(updatedGenre);
    }

    @DeleteMapping("/{id}")
    @HasPermission(Permission.DELETE_GENRE)
    @Operation(summary = "Delete genre", security = {@SecurityRequirement(name = "bearerAuth")})
    public ResponseEntity<Void> deleteGenre(@PathVariable int id) {
        genreServiceImpl.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }

}

