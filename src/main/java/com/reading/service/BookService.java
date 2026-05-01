package com.reading.service;

import com.reading.model.Book;
import com.reading.model.Chapter;
import com.reading.repository.BookRepository;
import com.reading.repository.ChapterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;

    public BookService(BookRepository bookRepository, ChapterRepository chapterRepository) {
        this.bookRepository = bookRepository;
        this.chapterRepository = chapterRepository;
    }

    public List<Book> getAllPublishedBooks() {
        return bookRepository.findByPublishedTrue();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCaseAndPublishedTrue(keyword);
    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategoryAndPublishedTrue(category);
    }

    public List<Chapter> getBookChapters(Long bookId) {
        return chapterRepository.findByBookIdOrderByChapterNumberAsc(bookId);
    }

    public Chapter getChapter(Long bookId, Integer chapterNumber) {
        return chapterRepository.findByBookIdAndChapterNumber(bookId, chapterNumber).orElse(null);
    }

    public Chapter getChapterById(Long chapterId) {
        return chapterRepository.findById(chapterId).orElse(null);
    }

    public Chapter saveChapter(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    public long getChapterCount(Long bookId) {
        return chapterRepository.countByBookId(bookId);
    }
}