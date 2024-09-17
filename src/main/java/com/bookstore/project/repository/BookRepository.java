package com.bookstore.project.repository;

import com.bookstore.project.dto.BookDTO;
import com.bookstore.project.entity.Book;
import com.bookstore.project.entity.Genre;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByGenreId(int genreId);
    List<Book> findByUserId(int userId);
}

