package com.example.salon.repository;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.Client;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    @Before
    public void setUp() {
        String firstName = "Dori";
        String lastName = "Dietrich";
        String email = "patrica@keeling.net";
        String phone = "(272) 301-6356";
        String gender = "Male";

        client = clientRepository.save(new Client(firstName, lastName, email, phone, gender));
    }

    @Test
    public void givenAnAppointment_saveIt_thenRetrievedItFromDBTest() {
        Date startTime = new Date();
        Date endTime = new Date();

        Appointment appointment = new Appointment(client, startTime, endTime);

        appointmentRepository.save(appointment);


        List<Appointment> appointments = appointmentRepository.findAll();
        Assertions.assertThat(appointments).isNotEmpty();
        Assertions.assertThat(appointments).size().isEqualTo(1);

        Appointment savedAppointment = appointments.get(0);

        Assertions.assertThat(savedAppointment.getId()).isNotNull();
        Assertions.assertThat(savedAppointment.getClient()).isEqualTo(client);
        Assertions.assertThat(savedAppointment.getStartTime()).isEqualTo(startTime);
        Assertions.assertThat(savedAppointment.getEndTime()).isEqualTo(endTime);
    }
}