package com.example.salon.controller;

import com.example.salon.SalonApplication;
import com.example.salon.dto.ClientDTO;
import com.example.salon.exceptions.EntityNotFoundException;
import com.example.salon.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SalonApplication.class)
@AutoConfigureMockMvc
public class ClientsControllerTest {

    private static final String FIRST_NAME = "name1";
    private static final String LAST_NAME = "lastName1";
    private static final String EMAIL = "a1@a1.com";
    private static final String PHONE = "123";
    private static final String GENDER = "Male";

    @MockBean
    private ClientService clientService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createTest() throws Exception {
        ClientDTO dto = new ClientDTO(FIRST_NAME, LAST_NAME, EMAIL, PHONE, GENDER);

        String content = objectMapper.writeValueAsString(dto);

        ArgumentCaptor<ClientDTO> captor = ArgumentCaptor.forClass(ClientDTO.class);

        Mockito.doNothing().when(clientService).create(captor.capture());

        mvc.perform(post("/api/v1/clients").content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());

        ClientDTO clientDTO = captor.getValue();

        Assertions.assertThat(clientDTO.getFirstName()).isEqualTo(FIRST_NAME);
        Assertions.assertThat(clientDTO.getLastName()).isEqualTo(LAST_NAME);
        Assertions.assertThat(clientDTO.getEmail()).isEqualTo(EMAIL);
        Assertions.assertThat(clientDTO.getPhone()).isEqualTo(PHONE);
        Assertions.assertThat(clientDTO.getGender()).isEqualTo(GENDER);

        Mockito.verify(clientService).create(clientDTO);
    }


    @Test
    public void getByIdTest() throws Exception {
        String clientId = "123";

        ClientDTO clientDTO = new ClientDTO(FIRST_NAME, LAST_NAME, EMAIL, PHONE, GENDER);

        given(clientService.getById(clientId)).willReturn(clientDTO);

        mvc.perform(get("/api/v1/clients/{clientId}", clientId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.phone", is(PHONE)))
                .andExpect(jsonPath("$.gender", is(GENDER)))
                .andDo(print());

        Mockito.verify(clientService).getById(clientId);
    }

    @Test
    public void given_ClientId_then_ClientNotFound_And_HandleException() throws Exception {
        String clientId = "123";

        String errorMessage = "ClientId not found";
        given(clientService.getById(clientId)).willThrow(new EntityNotFoundException(errorMessage));

        mvc.perform(get("/api/v1/clients/{clientId}", clientId).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

}