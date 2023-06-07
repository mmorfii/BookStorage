package com.example.bookstore.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "comments", schema = "jpa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String content;

    private LocalDateTime sendTime;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY)
    private Book book;
}
