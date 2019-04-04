package com.example.salon.service.impl;

import com.example.salon.domain.FileType;
import com.example.salon.service.ParserService;
import com.example.salon.service.StorageService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class StorageServiceImplTest {

    private StorageService storageService;

    private ParserService parserService = Mockito.mock(ParserService.class);
    private FileType fileType = FileType.CLIENTS;
    private MultipartFile file;

    @Before
    public void setUp() {
        this.storageService = new StorageServiceImpl(parserService);
        String text = "id,first_name,last_name,email,phone,gender,banned\n" +
                "e0b8ebfc-6e57-4661-9546-328c644a3764,Dori,Dietrich,patrica@keeling.net,(272) 301-6356,Male,false";
        file = new MockMultipartFile("file", "clientes.csv", "csv/plain", text.getBytes());
    }

    @Test
    public void storeTest() {
        ArgumentCaptor<FileType> fileTypeCaptor = ArgumentCaptor.forClass(FileType.class);
        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);

        Mockito.doNothing().when(parserService).process(fileTypeCaptor.capture(), fileCaptor.capture());

        this.storageService.store(fileType, file);

        Assertions.assertThat(fileTypeCaptor.getValue()).isEqualTo(fileType);

        Mockito.verify(parserService).process(fileTypeCaptor.getValue(), fileCaptor.getValue());
    }
}
