package com.example.bookstore.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "rating", schema = "jpa")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    private double rating;
}
