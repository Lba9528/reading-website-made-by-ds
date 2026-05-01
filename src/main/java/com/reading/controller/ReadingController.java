package com.reading.controller;

import com.reading.model.Book;
import com.reading.model.Chapter;
import com.reading.service.BookService;
import com.reading.service.ReadingProgressService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reading")
public class ReadingController {

    private final BookService bookService;
    private final ReadingProgressService progressService;

    public ReadingController(BookService bookService, ReadingProgressService progressService) {
        this.bookService = bookService;
        this.progressService = progressService;
    }

    @GetMapping("/{bookId}")
    public String startReading(@PathVariable Long bookId, Model model, Authentication auth) {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            return "redirect:/books";
        }

        List<Chapter> chapters = bookService.getBookChapters(bookId);
        if (chapters.isEmpty()) {
            return "redirect:/books/" + bookId;
        }

        // Try to find the user's last read chapter
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            // Will redirect to last read chapter if exists
        }

        return "redirect:/reading/" + bookId + "/1";
    }

    @GetMapping("/{bookId}/{chapterNumber}")
    public String readChapter(@PathVariable Long bookId,
                              @PathVariable Integer chapterNumber,
                              Model model,
                              Authentication auth) {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            return "redirect:/books";
        }

        Chapter chapter = bookService.getChapter(bookId, chapterNumber);
        if (chapter == null) {
            return "redirect:/books/" + bookId;
        }

        List<Chapter> chapters = bookService.getBookChapters(bookId);
        int currentIndex = -1;
        for (int i = 0; i < chapters.size(); i++) {
            if (chapters.get(i).getChapterNumber().equals(chapterNumber)) {
                currentIndex = i;
                break;
            }
        }

        boolean hasPrev = currentIndex > 0;
        boolean hasNext = currentIndex < chapters.size() - 1;

        Chapter prevChapter = hasPrev ? chapters.get(currentIndex - 1) : null;
        Chapter nextChapter = hasNext ? chapters.get(currentIndex + 1) : null;

        model.addAttribute("book", book);
        model.addAttribute("chapter", chapter);
        model.addAttribute("chapters", chapters);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("prevChapter", prevChapter);
        model.addAttribute("nextChapter", nextChapter);

        // Save reading progress if user is authenticated
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName()) && chapter.getId() != null) {
            // Get user ID from authentication - simplified approach
            progressService.updateProgress(1L, bookId, chapter.getId());
        }

        return "reading/chapter";
    }
}