package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRatingRepository extends JpaRepository<BookRating, Long> {

    List<BookRating> findBookRatingsByBook_Id(Long book_id);

    BookRating findByUser_IdAndBook_Id(Long user_id, Long book_id);

    void deleteByBook(Book book);


}