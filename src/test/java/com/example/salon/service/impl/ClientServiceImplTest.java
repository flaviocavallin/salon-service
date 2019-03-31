package com.example.salon.service.impl;

import com.example.salon.domain.Client;
import com.example.salon.dto.ClientDTO;
import com.example.salon.exceptions.EntityNotFoundException;
import com.example.salon.factory.ClientDTOFactory;
import com.example.salon.repository.ClientRepository;
import com.example.salon.service.ClientService;
import com.example.salon.util.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ClientServiceImplTest extends IntegrationTest {

    private static final String FIRST_NAME = "name1";
    private static final String LAST_NAME = "lastName1";
    private static final String EMAIL = "a1@a1.com";
    private static final String PHONE = "123";
    private static final String GENDER = "Male";


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ClientService clientService;

    private ClientRepository clientRepository = Mockito.mock(ClientRepository.class);

    @Autowired
    private ClientDTOFactory clientDTOFactory;

    @Before
    public void setUp() {
        this.clientService = new ClientServiceImpl(clientRepository, clientDTOFactory);
    }

    @Test
    public void givenClientDTO_then_createClientAndSaveItTest() {

        ClientDTO clientDTO = new ClientDTO(FIRST_NAME, LAST_NAME, EMAIL, PHONE, GENDER);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);

        Mockito.when(clientRepository.save(captor.capture())).thenReturn(Mockito.any(Client.class));

        this.clientService.create(clientDTO);

        Client client = captor.getValue();
        Assertions.assertThat(client.getFirstName()).isEqualTo(FIRST_NAME);
        Assertions.assertThat(client.getLastName()).isEqualTo(LAST_NAME);
        Assertions.assertThat(client.getEmail()).isEqualTo(EMAIL);
        Assertions.assertThat(client.getPhone()).isEqualTo(PHONE);
        Assertions.assertThat(client.getGender()).isEqualTo(GENDER);

        Mockito.verify(clientRepository).save(client);
    }


    @Test
    public void givenClientId_then_findClient_And_ReturnClientDTO() {
        String clientId = "123";

        Client client = new Client(FIRST_NAME, LAST_NAME, EMAIL, PHONE, GENDER);

        Mockito.when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        ClientDTO clientDTO = this.clientService.getById(clientId);

        Assertions.assertThat(clientDTO.getFirstName()).isEqualTo(FIRST_NAME);
        Assertions.assertThat(clientDTO.getLastName()).isEqualTo(LAST_NAME);
        Assertions.assertThat(clientDTO.getEmail()).isEqualTo(EMAIL);
        Assertions.assertThat(clientDTO.getPhone()).isEqualTo(PHONE);
        Assertions.assertThat(clientDTO.getGender()).isEqualTo(GENDER);

        Mockito.verify(clientRepository).findById(clientId);
    }


    @Test
    public void givenClientId_then_findClient_then_ThrowExceptionBecauseClientNotFound() {
        String clientId = "123";
        String exceptionMessage = "ClientId not found";
        Mockito.when(clientRepository.findById(clientId)).thenThrow(new EntityNotFoundException(exceptionMessage));

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage(exceptionMessage);

        this.clientService.getById(clientId);

        Mockito.verify(clientRepository).findById(clientId);
    }
}