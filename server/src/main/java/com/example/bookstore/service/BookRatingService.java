package com.example.bookstore.service;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.BookRating;
import com.example.bookstore.entity.User;
import com.example.bookstore.repository.BookRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookRatingService {
    @Autowired
    private BookRatingRepository bookRatingRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    public BookRating save(BookRating bookRating) {
        return bookRatingRepository.save(bookRating);
    }

    void deleteAllBookRating(Long book_id) {
        bookRatingRepository.deleteByBook(bookService.findBookById(book_id));
    }

    public List<BookRating> findBookRatingsByBook_Id(Long book_id) {
        return bookRatingRepository.findBookRatingsByBook_Id(book_id);
    }

    public BookRating findByUser_IdAndBook_Id(Long user_id, Long book_id) {
        return bookRatingRepository.findByUser_IdAndBook_Id(user_id, book_id);
    }

    public double findUsersBookRating(Authentication authentication, Long book_id) {
        User user = userService.getUserAuth(authentication);
        BookRating bookRating = findByUser_IdAndBook_Id(user.getId(), book_id);
        if (bookRating != null) {
            return bookRating.getRating();
        } else return 0.0;
    }

    public double rateBook(Authentication authentication, Long book_id, double rating) {
        User user = userService.getUserAuth(authentication);
        BookRating bookRating = findByUser_IdAndBook_Id(user.getId(), book_id);
        Book book = bookService.findBookById(book_id);
        if (bookRating != null) {
            bookRating.setRating(rating);
            save(bookRating);
        } else {
            BookRating newBookRating = BookRating.builder()
                    .user(user)
                    .book(book)
                    .rating(rating)
                    .build();
            save(newBookRating);
        }
        return countBookRating(book_id);
    }

    public double countBookRating(Long book_id) {
        return findBookRatingsByBook_Id(book_id)
                .stream()
                .mapToDouble(BookRating::getRating)
                .average()
                .orElse(Double.NaN);
    }


}
