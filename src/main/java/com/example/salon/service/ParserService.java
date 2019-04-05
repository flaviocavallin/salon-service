package com.example.salon.service;

import com.example.salon.domain.FileType;
import com.example.salon.service.parser.ParserConsumer;
import org.simpleflatmapper.util.CheckedConsumer;

import java.io.File;

public interface ParserService {

    void process(FileType fileType, File file);
}
