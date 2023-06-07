package com.example.bookstore.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Builder
@Table(name = "books", schema = "jpa")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 3, message = "Книга не может содержать менее 3-ёх символов в названии")
    private String title;

    @NotBlank(message = "Поле не может быть пустым")
    private String description;


    @OneToOne(targetEntity = Media.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "bytes", allowSetters = true)
    private Media picture;

    private LocalDate publishDate;

    @NotBlank(message = "Поле не может быть пустым")
    private String genre;

    @NotBlank(message = "Поле не может быть пустым")
    private String author;

}
