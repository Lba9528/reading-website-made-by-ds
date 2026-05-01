package com.reading.service;

import com.reading.model.ReadingProgress;
import com.reading.repository.ReadingProgressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadingProgressService {

    private final ReadingProgressRepository progressRepository;

    public ReadingProgressService(ReadingProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    public ReadingProgress getProgress(Long userId, Long bookId) {
        return progressRepository.findByUserIdAndBookId(userId, bookId).orElse(null);
    }

    public void saveProgress(ReadingProgress progress) {
        progressRepository.save(progress);
    }

    public List<ReadingProgress> getUserReadingHistory(Long userId) {
        return progressRepository.findByUserIdOrderByLastReadAtDesc(userId);
    }

    public void updateProgress(Long userId, Long bookId, Long chapterId) {
        ReadingProgress progress = progressRepository.findByUserIdAndBookId(userId, bookId)
                .orElse(ReadingProgress.builder()
                        .user(com.reading.model.User.builder().id(userId).build())
                        .book(com.reading.model.Book.builder().id(bookId).build())
                        .build());
        progress.setCurrentChapterId(chapterId);
        progressRepository.save(progress);
    }
}