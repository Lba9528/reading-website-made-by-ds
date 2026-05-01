package com.reading.repository;

import com.reading.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByPublishedTrue();
    List<Book> findByCategoryAndPublishedTrue(String category);
    List<Book> findByTitleContainingIgnoreCaseAndPublishedTrue(String keyword);
    List<Book> findByAuthorContainingIgnoreCaseAndPublishedTrue(String author);
}