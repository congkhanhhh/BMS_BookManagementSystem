package com.bookstore.project.repository;

import com.bookstore.project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUserId(long userId);
    List<Book> findByGenreId(int genreId);
}


