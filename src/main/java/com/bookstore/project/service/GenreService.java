package com.bookstore.project.service;

import com.bookstore.project.entity.Genre;
import com.bookstore.project.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public Genre addGenre(Genre genre) {
        // Đảm bảo rằng genre không null hoặc thiếu dữ liệu
        if (genre.getName() == null || genre.getName().isEmpty()) {
            throw new IllegalArgumentException("Genre name cannot be empty");
        }

        return genreRepository.save(genre);
    }

        public List<Genre> getAllGenres() {
            return genreRepository.findAll();
        }

        public Optional<Genre> getGenreById(long id) {
            return genreRepository.findById(id);
        }

        public Genre saveGenre(Genre genre) {
            return genreRepository.save(genre);
        }

        public void deleteGenre(long id) {
            genreRepository.deleteById(id);
        }
    }



