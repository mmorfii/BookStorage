package com.example.bookstore.service;

import com.example.bookstore.entity.Media;
import com.example.bookstore.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    public Optional<Media> findMediaById(Long id) {
        return mediaRepository.findMediaById(id);
    }


    public Media mapMultipartFileToMedia(MultipartFile multipartFile) throws IOException {
        return Media.builder()
                .originalFileName(multipartFile.getOriginalFilename())
                .mediaType(multipartFile.getContentType())
                .size(multipartFile.getSize())
                .bytes(multipartFile.getBytes()).build();
    }
}
