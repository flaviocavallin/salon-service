package com.example.salon.service.impl;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.Client;
import com.example.salon.domain.Treatment;
import com.example.salon.dto.AppointmentDTO;
import com.example.salon.dto.TreatmentDTO;
import com.example.salon.repository.AppointmentRepository;
import com.example.salon.repository.ClientRepository;
import com.example.salon.service.AppointmentService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppointmentServiceImplTest {

    private static final String CLIENT_ID = "123";
    private static final String FIRST_NAME = "name1";
    private static final String LAST_NAME = "lastName1";
    private static final String EMAIL = "a1@a1.com";
    private static final String PHONE = "123";
    private static final String GENDER = "Male";

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private AppointmentService appointmentService;
    private AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
    private ClientRepository clientRepository = mock(ClientRepository.class);

    @Before
    public void setUp() {
        appointmentService = new AppointmentServiceImpl(appointmentRepository, clientRepository);
    }

    @Test
    public void given_ClientDTO_then_createClient() {
        Client client = new Client(FIRST_NAME, LAST_NAME, EMAIL, PHONE, GENDER);

        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));


        ArgumentCaptor<Appointment> captor = ArgumentCaptor.forClass(Appointment.class);

        when(appointmentRepository.save(captor.capture())).thenReturn(any(Appointment.class));

        String treatmentName = "Eyebrow Tint";
        double treatmentPrice = 7;
        long treatmentLoyaltyPoint = 10;

        TreatmentDTO treatmentDTO = new TreatmentDTO(treatmentName, treatmentPrice, treatmentLoyaltyPoint);

        Date startTime = new Date();
        Date endTime = new Date();

        AppointmentDTO appointmentDTO = new AppointmentDTO(CLIENT_ID, startTime, endTime, Arrays.asList(treatmentDTO));

        appointmentService.create(appointmentDTO);

        Appointment appointment = captor.getValue();


        Assertions.assertThat(appointment.getClient()).isEqualToComparingFieldByField(client);
        Assertions.assertThat(appointment.getStartTime()).isEqualTo(startTime);
        Assertions.assertThat(appointment.getEndTime()).isEqualTo(endTime);
        Assertions.assertThat(appointment.getPurchases()).isEmpty();
        Assertions.assertThat(appointment.getTreatments().size()).isEqualTo(1);


        Treatment treatment = appointment.getTreatments().get(0);

        Assertions.assertThat(treatment.getName()).isEqualTo(treatmentName);
        Assertions.assertThat(treatment.getPrice()).isEqualTo(treatmentPrice);
        Assertions.assertThat(treatment.getLoyaltyPoints()).isEqualTo(treatmentLoyaltyPoint);

        verify(clientRepository).findById(CLIENT_ID);
        verify(appointmentRepository).save(appointment);
    }


    @Test
    public void given_appointmentId_then_callDelete() {
        String appointmentId = "123";

        this.appointmentService.deleteById(appointmentId);

        verify(appointmentRepository).deleteById(appointmentId);
    }

    @Test
    public void given_appointmentId_then_call_repository_and_return_Appointment() {
        String appointmentId = "123";

        Appointment appointment = mock(Appointment.class);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        Optional<Appointment> opAppointment = this.appointmentService.findById(appointmentId);

        Assertions.assertThat(opAppointment.isPresent()).isTrue();
        Assertions.assertThat(opAppointment.get()).isEqualTo(appointment);

        verify(appointmentRepository).findById(appointmentId);
    }

}
