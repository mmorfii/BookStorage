package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookById(Long id);

    @Override
    List<Book> findAll();

    @Query("select b from Book b where upper(b.title) like upper(concat('%',?1, '%'))")
    List<Book> findBooksBySearchline(String searchline);


    void deleteBookById(Long id);
}