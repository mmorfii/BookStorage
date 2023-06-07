package com.example.bookstore.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.example.bookstore.entity.User} entity
 */
@Data
@Builder
public class CredentialsDto implements Serializable {
    private final String username;
    private final String password;
}