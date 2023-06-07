package com.example.bookstore.repository;

import com.example.bookstore.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {

    Optional<Media> findMediaById(Long id);
}