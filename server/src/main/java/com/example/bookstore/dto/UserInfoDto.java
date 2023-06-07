package com.example.bookstore.dto;


import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Media;
import com.example.bookstore.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * A DTO for the {@link com.example.bookstore.entity.User} entity
 */
@Data
@Builder
public class UserInfoDto implements Serializable {

    private Long id;

    private final String username;
    private final Set<Role> roles;
    private final String email;
    private String name;
    private String surname;
    private LocalDate registrationDate;
    private Media avatar;

    private List<Book> books;
}