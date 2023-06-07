package com.example.bookstore.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Entity
@Builder
@Table(name = "users", schema = "jpa")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 3, message = "Никнейм не может содержать менее 3-ёх символов")
    @Size(max = 20, message = "Слишком длинный никнейм")
    @Column(name = "username", unique = true, length = 20)
    private String username;


    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Transient
    @JsonIgnore
    private String passwordConfirm;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();


    @Email(message = "Поле должно иметь формат эл.почты")
    @NotBlank(message = "Поле не может быть пустым")
    @Column(name = "email", unique = true, length = 40)
    private String email;


    @NotBlank(message = "Поле не должно быть путсым")
    @Column(name = "name")
    private String name;


    @NotBlank(message = "Поле не должно быть пустым")
    @Column(name = "surname")
    private String surname;


    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @OneToOne(targetEntity = Media.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "bytes", allowSetters = true)
    private Media avatar;

    @ManyToMany(targetEntity = Book.class)
    @JoinTable(
            name = "user_books",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")}
    )
    private List<Book> books = new ArrayList<>();


    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }


}

