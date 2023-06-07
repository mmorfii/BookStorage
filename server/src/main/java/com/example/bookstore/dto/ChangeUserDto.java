package com.example.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link com.example.bookstore.entity.User} entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeUserDto implements Serializable {
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 3, message = "Никнейм не может содержать менее 3-ёх символов")
    @Size(max = 20, message = "Слишком длинный никнейм")
    private String username;
    @Email(message = "Поле должно иметь формат эл.почты")
    @NotBlank(message = "Поле не может быть пустым")
    private String email;
    @NotBlank(message = "Поле не должно быть путсым")
    private String name;
    @NotBlank(message = "Поле не должно быть пустым")
    private String surname;
}