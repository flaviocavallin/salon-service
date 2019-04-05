package com.example.salon.controller;

import com.example.salon.domain.FileType;
import org.springframework.core.convert.converter.Converter;

public class FileTypeConverter implements Converter<String, FileType> {
    @Override
    public FileType convert(String fileType) {
        return FileType.getByName(fileType);
    }
}