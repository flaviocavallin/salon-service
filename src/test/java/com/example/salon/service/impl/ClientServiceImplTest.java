package com.example.salon.service.impl;

import com.example.salon.domain.Client;
import com.example.salon.domain.PointedClient;
import com.example.salon.dto.ClientDTO;
import com.example.salon.dto.PointedClientDTO;
import com.example.salon.exceptions.EntityCascadeDeletionNotAllowedException;
import com.example.salon.exceptions.EntityNotFoundException;
import com.example.salon.factory.ClientDTOFactory;
import com.example.salon.listeners.LoyaltyPointEvent;
import com.example.salon.repository.AppointmentRepository;
import com.example.salon.repository.ClientRepository;
import com.example.salon.service.ClientService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClientServiceImplTest {

    private static final String FIRST_NAME = "name1";
    private static final String LAST_NAME = "lastName1";
    private static final String EMAIL = "a1@a1.com";
    private static final String PHONE = "123";
    private static final String GENDER = "Male";
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private UUID clientId = UUID.randomUUID();
    private ClientService clientService;

    private ClientRepository clientRepository = mock(ClientRepository.class);
    private AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);

    private ClientDTOFactory clientDTOFactory = new ClientDTOFactory();

    @Before
    public void setUp() {
        this.clientService = new ClientServiceImpl(clientRepository, clientDTOFactory, appointmentRepository);
    }

    @Test
    public void givenClientDTO_then_createClientAndSaveItTest() {

        ClientDTO clientDTO = new ClientDTO(FIRST_NAME, LAST_NAME, EMAIL, PHONE, GENDER);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);

        when(clientRepository.save(captor.capture())).thenReturn(any(Client.class));

        this.clientService.save(clientDTO);

        Client client = captor.getValue();
        Assertions.assertThat(client.getFirstName()).isEqualTo(FIRST_NAME);
        Assertions.assertThat(client.getLastName()).isEqualTo(LAST_NAME);
        Assertions.assertThat(client.getEmail()).isEqualTo(EMAIL);
        Assertions.assertThat(client.getPhone()).isEqualTo(PHONE);
        Assertions.assertThat(client.getGender()).isEqualTo(GENDER);

        verify(clientRepository).save(client);
    }


    @Test
    public void givenClientId_then_findClient_And_ReturnClientDTO() {

        Client client = new Client(FIRST_NAME, LAST_NAME, EMAIL, PHONE, GENDER);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        ClientDTO clientDTO = this.clientService.getById(clientId);

        Assertions.assertThat(clientDTO.getFirstName()).isEqualTo(FIRST_NAME);
        Assertions.assertThat(clientDTO.getLastName()).isEqualTo(LAST_NAME);
        Assertions.assertThat(clientDTO.getEmail()).isEqualTo(EMAIL);
        Assertions.assertThat(clientDTO.getPhone()).isEqualTo(PHONE);
        Assertions.assertThat(clientDTO.getGender()).isEqualTo(GENDER);

        verify(clientRepository).findById(clientId);
    }


    @Test
    public void givenClientId_then_findClient_then_ThrowExceptionBecauseClientNotFound() {
        String exceptionMessage = "ClientId not found";
        when(clientRepository.findById(clientId)).thenThrow(new EntityNotFoundException(exceptionMessage));

        thrown.expect(EntityNotFoundException.class);
        thrown.expectMessage(exceptionMessage);

        this.clientService.getById(clientId);

        verify(clientRepository).findById(clientId);
    }


    @Test
    public void givenClientId_then_DeleteClienteById() {
        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        doNothing().when(clientRepository).deleteById(captor.capture());
        when(appointmentRepository.existsByClient_Id(clientId)).thenReturn(Boolean.FALSE);

        this.clientService.deleteById(clientId);

        Assertions.assertThat(captor.getValue()).isEqualTo(clientId);

        verify(clientRepository).deleteById(clientId);
        verify(appointmentRepository).existsByClient_Id(clientId);
    }


    @Test
    public void given_ClientId_then_tryToDeleteTheClient_And_ThrowExceptionBecauseItHasAReferenceToAppointment() {
        thrown.expect(EntityCascadeDeletionNotAllowedException.class);
        thrown.expectMessage("Impossible to delete the client because there are appointments");

        when(appointmentRepository.existsByClient_Id(clientId)).thenReturn(Boolean.TRUE);

        this.clientService.deleteById(clientId);

        verify(appointmentRepository).existsByClient_Id(clientId);
    }


    @Test
    public void give_LoyalClients_then_return_PointedClientDTO() {
        UUID id = UUID.randomUUID();
        String email = "a1@a1.com";
        long points = 100;

        PointedClient pointedClient = new PointedClient(id, email, points);

        LocalDate dateFrom = LocalDate.of(2019, 1, 2);
        LocalDate dateTo = LocalDate.of(2019, 1, 3);
        int limit = 10;


        when(clientRepository.getTopMostLoyalActiveClientsBy(dateFrom, dateTo, limit)).thenReturn(Arrays.asList(pointedClient));

        List<PointedClientDTO> pointedClientsDTO = clientService.getTopMostLoyalActiveClientsBy(dateFrom, dateTo,
                limit);

        Assertions.assertThat(pointedClientsDTO.size()).isEqualTo(1);

        PointedClientDTO dto = pointedClientsDTO.get(0);

        Assertions.assertThat(dto.getId()).isEqualTo(id);
        Assertions.assertThat(dto.getEmail()).isEqualTo(email);
        Assertions.assertThat(dto.getPoints()).isEqualTo(points);
    }


    @Test
    public void given_LoyaltyPointEvent_then_call_incrementLoyaltyPoints() {
        UUID clientId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2019, 1, 1);
        long points = 10;

        LoyaltyPointEvent loyaltyPointEvent = new LoyaltyPointEvent(this, clientId, date, points);
        this.clientService.incrementClientLoyaltyPoints(loyaltyPointEvent);

        verify(clientRepository).incrementLoyaltyPoints(clientId, date, points);
    }

}