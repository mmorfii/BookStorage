package com.example.bookstore.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * A DTO for the {@link com.example.bookstore.entity.Book} entity
 */
@Data
public class BookCreateDto {
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 3, message = "Книга не может содержать менее 3-ёх символов в названии")
    private String title;

    @NotBlank(message = "Поле не может быть пустым")
    private String description;

    @NotBlank(message = "Поле не может быть пустым")
    private String genre;

    @NotBlank(message = "Поле не может быть пустым")
    private String author;

    @NotNull(message = "Поле не может быть пустым")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate publishDate;
}
