package com.reading.controller;

import com.reading.model.Book;
import com.reading.model.Chapter;
import com.reading.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllPublishedBooks());
        return "books/list";
    }

    @GetMapping("/{id}")
    public String bookDetail(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "redirect:/books";
        }
        List<Chapter> chapters = bookService.getBookChapters(id);
        model.addAttribute("book", book);
        model.addAttribute("chapters", chapters);
        return "books/detail";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam String keyword, Model model) {
        model.addAttribute("books", bookService.searchBooks(keyword));
        model.addAttribute("keyword", keyword);
        return "books/list";
    }

    @GetMapping("/category/{category}")
    public String booksByCategory(@PathVariable String category, Model model) {
        model.addAttribute("books", bookService.getBooksByCategory(category));
        model.addAttribute("category", category);
        return "books/list";
    }
}