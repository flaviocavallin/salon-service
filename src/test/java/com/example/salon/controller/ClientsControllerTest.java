package com.example.salon.controller;

import com.example.salon.SalonApplication;
import com.example.salon.dto.ClientDTO;
import com.example.salon.dto.PointedClientDTO;
import com.example.salon.exceptions.EntityCascadeDeletionNotAllowedException;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClientsControllerTest {

    private static final UUID CLIENT_ID = UUID.randomUUID();

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

        Mockito.doNothing().when(clientService).save(captor.capture());

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

        Mockito.verify(clientService).save(clientDTO);
    }


    @Test
    public void getByIdTest() throws Exception {

        ClientDTO clientDTO = new ClientDTO(FIRST_NAME, LAST_NAME, EMAIL, PHONE, GENDER);

        given(clientService.getById(CLIENT_ID)).willReturn(clientDTO);

        mvc.perform(get("/api/v1/clients/{clientId}", CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.email", is(EMAIL)))
                .andExpect(jsonPath("$.phone", is(PHONE)))
                .andExpect(jsonPath("$.gender", is(GENDER)))
                .andDo(print());

        Mockito.verify(clientService).getById(CLIENT_ID);
    }

    @Test
    public void given_ClientId_then_ClientNotFound_And_HandleException() throws Exception {

        String errorMessage = "ClientId not found";
        given(clientService.getById(CLIENT_ID)).willThrow(new EntityNotFoundException(errorMessage));

        mvc.perform(get("/api/v1/clients/{clientId}", CLIENT_ID).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.message", is(errorMessage)));

        Mockito.verify(clientService).getById(CLIENT_ID);
    }

    @Test
    public void given_ClientId_then_deleteClientById() throws Exception {

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Mockito.doNothing().when(clientService).deleteById(captor.capture());

        mvc.perform(delete("/api/v1/clients/{clientId}", CLIENT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertThat(captor.getValue()).isEqualTo(CLIENT_ID);

        Mockito.verify(clientService).deleteById(CLIENT_ID);
    }

    @Test
    public void given_ClientId_then_tryDeleteClientById_And_ThrowException_BecauseHasReferenceToAppointment() throws Exception {

        String errorMessage = "Impossible to delete the client because there are appointments";
        Mockito.doThrow(new EntityCascadeDeletionNotAllowedException(errorMessage)).when(clientService).deleteById(CLIENT_ID);

        mvc.perform(delete("/api/v1/clients/{clientId}", CLIENT_ID).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.message", is(errorMessage)));

        Mockito.verify(clientService).deleteById(CLIENT_ID);
    }


    @Test
    public void given_limit_and_dateFrom_then_getTopLoyalClients() throws Exception {

        int limit = 10;
        LocalDate dateFrom = LocalDate.of(2019, 1, 1);

        UUID id = UUID.randomUUID();
        String email = "a1@a1.com";
        Long points = 10l;

        PointedClientDTO dto = new PointedClientDTO(id, email, points);

        given(clientService.getTopMostLoyalActiveClientsBy(dateFrom, LocalDate.now(), limit)).willReturn(Arrays.asList(dto));

        mvc.perform(get("/api/v1/clients/top/{limit}/loyal", limit).param("dateFrom", dateFrom.toString()).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id", is(id.toString())))
                .andExpect(jsonPath("$[0].email", is(email)))
                .andExpect(jsonPath("$[0].points", is(points.intValue())))
                .andDo(print());

        Mockito.verify(clientService).getTopMostLoyalActiveClientsBy(dateFrom, LocalDate.now(), limit);
    }

}