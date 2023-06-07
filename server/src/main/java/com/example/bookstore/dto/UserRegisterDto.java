package com.example.bookstore.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link com.example.bookstore.entity.User} entity
 */
@Data
@Builder
public class UserRegisterDto implements Serializable {
    @NotBlank(message = "Поле не должно быть путсым")
    private final String name;
    @NotBlank(message = "Поле не должно быть пустым")
    private final String surname;
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 3, message = "Никнейм не может содержать менее 3-ёх символов")
    @Size(max = 20, message = "Слишком длинный никнейм")
    private final String username;
    @Email(message = "Поле должно иметь формат эл.почты")
    @NotBlank(message = "Поле не может быть пустым")
    private final String email;
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private final String password;

    @NotBlank(message = "Поле не может быть пустым")
    private final String passwordConfirm;

}