package com.example.bookstore.dto;


import com.example.bookstore.entity.Media;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * A DTO for the {@link com.example.bookstore.entity.Book} entity
 */
@Data
@Builder
public class BookInfoDto {
    private Long id;

    private String title;

    private String description;

    private Media picture;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate publishDate;

    private String genre;

    private String author;

    private double rating;
}
