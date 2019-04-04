package com.example.salon.service;

import com.example.salon.domain.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void store(FileType fileType, MultipartFile file);
}
