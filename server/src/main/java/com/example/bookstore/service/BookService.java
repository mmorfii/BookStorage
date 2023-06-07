package com.example.bookstore.service;


import com.example.bookstore.dto.BookCreateDto;
import com.example.bookstore.dto.BookInfoDto;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Media;
import com.example.bookstore.entity.User;
import com.example.bookstore.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Validator;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private BookRatingService bookRatingService;

    @Autowired
    private Validator validator;

    public Book findBookById(Long id) {
        return bookRepository.findBookById(id).orElse(null);
    }

    public BookInfoDto findBookInfoById(Long id) {
        Book book = findBookById(id);
        if (book != null) {
            return mapBookToBookInfoDto(book);
        } else {
            return null;
        }
    }

    public Page<BookInfoDto> findBooks(Pageable pageable) {
        if (pageable.getSort().getOrderFor("rating") != null) {
            List<BookInfoDto> bookInfoDtoList = bookRepository.findAll().stream()
                    .sorted(Comparator.comparingDouble((Book a) -> {
                        double rating = bookRatingService.countBookRating(a.getId());
                        System.out.println(rating);
                        return Double.isNaN(rating) ? -1 : rating;
                    }).reversed())
                    .map(this::mapBookToBookInfoDto)
                    .limit(pageable.getPageSize())
                    .toList();
            return new PageImpl<>(bookInfoDtoList, pageable, bookRepository.findAll().size());
        } else {
            List<BookInfoDto> bookInfoDtoList = bookRepository.findAll(pageable).stream()
                    .map(this::mapBookToBookInfoDto)
                    .toList();
            return new PageImpl<>(bookInfoDtoList, pageable, bookRepository.findAll().size());
        }

    }

    public BookInfoDto findTopBook() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            return null;
        }
        Book topBook = books
                .stream()
                .filter(book -> !Double.isNaN(bookRatingService.countBookRating(book.getId())))
                .max(Comparator.comparing(book -> bookRatingService.countBookRating(book.getId())))
                .orElse(books.get(0));
        return mapBookToBookInfoDto(topBook);

    }


    public List<BookInfoDto> findBooksBySearchline(String searchline) {
        return bookRepository.findBooksBySearchline(searchline).stream()
                .map(this::mapBookToBookInfoDto)
                .toList();

    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book mapBookCreateDtoToBook(BookCreateDto bookCreateDto) {
        return Book.builder()
                .title(bookCreateDto.getTitle())
                .author(bookCreateDto.getAuthor())
                .genre(bookCreateDto.getGenre())
                .description(bookCreateDto.getDescription())
                .publishDate(bookCreateDto.getPublishDate())
                .build();
    }

    public BookInfoDto mapBookToBookInfoDto(Book book) {
        double rating = bookRatingService.countBookRating(book.getId());
        return BookInfoDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .description(book.getDescription())
                .publishDate(book.getPublishDate())
                .picture(book.getPicture())
                .rating(rating)
                .build();
    }

    public ResponseEntity<?> createBook(MultipartFile multipartFile, String jsonBook) throws IOException {
        BookCreateDto bookCreateDto = new ObjectMapper().readValue(jsonBook, BookCreateDto.class);
        SpringValidatorAdapter springValidator = new SpringValidatorAdapter(validator);
        BindingResult bindingResult = new BeanPropertyBindingResult(bookCreateDto, "bookCreateDto");
        springValidator.validate(bookCreateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldErrors(), HttpStatus.CONFLICT);
        }
        if (bookCreateDto.getPublishDate().isAfter(LocalDate.now())) {
            bindingResult.addError(new FieldError("bookCreateDto", "publishDate", "Книга должна быть издана на данный момент времени"));
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldErrors(), HttpStatus.CONFLICT);
        }
        Book book = mapBookCreateDtoToBook(bookCreateDto);
        if (multipartFile != null) {
            Media media = mediaService.mapMultipartFileToMedia(multipartFile);
            book.setPicture(media);
        }
        return new ResponseEntity<>(mapBookToBookInfoDto(save(book)), HttpStatus.CREATED);

    }

    public ResponseEntity<?> updateBook(MultipartFile multipartFile, String jsonBook, Long id) throws IOException {
        BookCreateDto bookCreateDto = new ObjectMapper().readValue(jsonBook, BookCreateDto.class);
        SpringValidatorAdapter springValidator = new SpringValidatorAdapter(validator);
        BindingResult bindingResult = new BeanPropertyBindingResult(bookCreateDto, "bookCreateDto");
        springValidator.validate(bookCreateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldErrors(), HttpStatus.CONFLICT);
        }
        if (bookCreateDto.getPublishDate().isAfter(LocalDate.now())) {
            bindingResult.addError(new FieldError("bookCreateDto", "publishDate", "Книга должна быть издана на данный момент времени"));
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldErrors(), HttpStatus.CONFLICT);
        }
        Book oldBook = findBookById(id);
        Book book = mapBookCreateDtoToBook(bookCreateDto);
        book.setId(id);
        book.setPicture(oldBook.getPicture());
        if (multipartFile != null) {
            Media media = mediaService.mapMultipartFileToMedia(multipartFile);
            book.setPicture(media);
        }
        return new ResponseEntity<>(mapBookToBookInfoDto(save(book)), HttpStatus.OK);
    }


    public void deleteBookById(Long id) {
        bookRatingService.deleteAllBookRating(id);
        Book book = findBookById(id);
        userService.findAll().forEach(user -> {
            user.getBooks().remove(book);
            userService.save(user);
        });
        bookRepository.deleteBookById(id);
    }

    public User addToFavourite(Authentication authentication, Long book_id, boolean add) {
        User user = userService.getUserAuth(authentication);
        Book book = findBookById(book_id);
        if (add) {
            user.getBooks().add(book);
        } else {
            user.getBooks().remove(book);
        }
        userService.save(user);
        return user;
    }


}
