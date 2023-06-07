package com.example.bookstore.service;

import com.example.bookstore.entity.Role;
import com.example.bookstore.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class AdminInitService implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userService.existsByUsername("Admin")) {
            User user = User.builder()
                    .username("Admin")
                    .email("example@gmail.com")
                    .name("Admin")
                    .surname("Admin")
                    .password(passwordEncoder.encode("adminpass"))
                    .roles(Set.of(Role.ROLE_ADMIN))
                    .registrationDate(LocalDate.now())
                    .build();
            userService.save(user);
        }
    }
}
