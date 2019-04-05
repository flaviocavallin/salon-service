package com.example.salon.controller;

import com.example.salon.domain.FileType;
import com.example.salon.service.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileUploadControllerTest {

    private FileUploadController controller;

    @MockBean
    private StorageService storageService;

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() {
        this.controller = new FileUploadController(storageService);
    }

    @Test
    public void handleFileUploadTest() throws Exception {
        FileType fileType = FileType.CLIENTS;
        String text = "id,first_name,last_name,email,phone,gender,banned\n" +
                "e0b8ebfc-6e57-4661-9546-328c644a3764,Dori,Dietrich,patrica@keeling.net,(272) 301-6356,Male,false";
        MockMultipartFile file = new MockMultipartFile("file", "clientes.csv", "text/plain", text.getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/files/{fileType}", fileType.getName())
                .file(file))
                .andExpect(status().is(200))
                .andDo(print());
        Mockito.verify(storageService).store(fileType, file);
    }
}
