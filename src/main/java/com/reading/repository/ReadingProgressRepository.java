package com.reading.repository;

import com.reading.model.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {
    Optional<ReadingProgress> findByUserIdAndBookId(Long userId, Long bookId);
    List<ReadingProgress> findByUserIdOrderByLastReadAtDesc(Long userId);
}