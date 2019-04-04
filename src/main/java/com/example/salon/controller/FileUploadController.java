package com.example.salon.controller;

import com.example.salon.domain.FileType;
import com.example.salon.service.StorageService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1/files")
public class FileUploadController {

    private final StorageService storageService;

    FileUploadController(StorageService storageService) {
        this.storageService = Objects.requireNonNull(storageService, "storageService can not be null");
    }

    @PostMapping("/{fileType}")
    public void handleFileUpload(@PathVariable("fileType") FileType fileType,
                                 @RequestParam("file") MultipartFile file) {
        storageService.store(fileType, file);
    }
}
