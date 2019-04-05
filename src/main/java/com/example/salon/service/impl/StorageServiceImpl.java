package com.example.salon.service.impl;

import com.example.salon.domain.FileType;
import com.example.salon.service.ParserService;
import com.example.salon.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

    private final Path storeLocation;
    private ParserService parserService;

    StorageServiceImpl(ParserService parserService) {
        this.parserService = Objects.requireNonNull(parserService, "parserService can not be null");
        this.storeLocation = Paths.get(System.getProperty("java.io.tmpdir"));
    }

    @Override
    public void store(FileType fileType, MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                LOGGER.error("ailed to store empty file={}", filename);
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                throw new StorageException("Failed to store file=" + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.storeLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            Path p = this.storeLocation.resolve(filename);
            File f = new File(p.toAbsolutePath().toString());
            parserService.process(fileType, f);

        } catch (IOException e) {
            LOGGER.error("Failed to store file={}", filename, e);
            throw new StorageException("Failed to store file=" + filename);
        }
    }
}