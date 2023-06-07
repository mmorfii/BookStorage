package com.example.bookstore.controller;


import com.example.bookstore.service.BookRatingService;
import com.example.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRatingService bookRatingService;

    @GetMapping()
    public ResponseEntity<?> findBooksPage(@PageableDefault(size = 4, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(bookService.findBooks(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findBookInfoById(id));
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<?> findUsersBookRating(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(bookRatingService.findUsersBookRating(authentication, id));
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<?> rateBook(Authentication authentication, @PathVariable Long id, @RequestParam("rating") Double rating) {
        return ResponseEntity.ok(bookRatingService.rateBook(authentication, id, rating));
    }

    @GetMapping("/search")
    public ResponseEntity<?> findBookById(@RequestParam("searchline") String searchline) {
        return ResponseEntity.ok(bookService.findBooksBySearchline(searchline));
    }

    @GetMapping("/top")
    public ResponseEntity<?> findBookById() {
        return ResponseEntity.ok(bookService.findTopBook());
    }

    @PostMapping()
    public ResponseEntity<?> createBook(@RequestParam(value = "picture", required = false) MultipartFile multipartFile, @RequestParam(value = "book", required = false) String jsonBook) throws IOException {
        return bookService.createBook(multipartFile, jsonBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBookById(@RequestParam(value = "picture", required = false) MultipartFile multipartFile, @RequestParam(value = "book", required = false) String jsonBook, @PathVariable Long id) throws IOException {
        return bookService.updateBook(multipartFile, jsonBook, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/favourite")
    public ResponseEntity<?> favouriteBook(Authentication authentication, @PathVariable Long id, @RequestParam("add") boolean add) {
        return new ResponseEntity<>(bookService.addToFavourite(authentication, id, add), HttpStatus.OK);
    }


}
