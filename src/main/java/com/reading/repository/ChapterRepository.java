package com.reading.repository;

import com.reading.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByBookIdOrderByChapterNumberAsc(Long bookId);
    Optional<Chapter> findByBookIdAndChapterNumber(Long bookId, Integer chapterNumber);
    Optional<Chapter> findTopByBookIdOrderByChapterNumberDesc(Long bookId);
    long countByBookId(Long bookId);
}