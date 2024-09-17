package com.bookstore.project.repository;

import com.bookstore.project.entity.Book;
import com.bookstore.project.entity.Favorite;
import com.bookstore.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserAndBook(User user, Book book);
    Optional<Favorite> findByUserAndBook(User user, Book book);
    List<Favorite> findByUser(User user);
}

