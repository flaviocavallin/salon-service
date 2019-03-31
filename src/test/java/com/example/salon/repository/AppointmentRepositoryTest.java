package com.example.salon.repository;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.Client;
import com.example.salon.domain.Purchase;
import com.example.salon.domain.Treatment;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
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

        String treatmentName = "Full Head Colour";
        double treatmentPrice = 85;
        long treatmentLoyaltyPoints = 20;

        Treatment treatment = new Treatment(treatmentName, treatmentPrice, treatmentLoyaltyPoints);

        Appointment appointment = new Appointment(client, startTime, endTime, Arrays.asList(treatment));

        appointmentRepository.save(appointment);


        List<Appointment> appointments = appointmentRepository.findAll();
        Assertions.assertThat(appointments).isNotEmpty();
        Assertions.assertThat(appointments).size().isEqualTo(1);

        Appointment savedAppointment = appointments.get(0);

        Assertions.assertThat(savedAppointment.getId()).isNotNull();
        Assertions.assertThat(savedAppointment.getClient()).isEqualTo(client);
        Assertions.assertThat(savedAppointment.getStartTime()).isEqualTo(startTime);
        Assertions.assertThat(savedAppointment.getEndTime()).isEqualTo(endTime);
        Assertions.assertThat(savedAppointment.getTreatments()).size().isEqualTo(1);


        Treatment savedTreatment = savedAppointment.getTreatments().get(0);

        Assertions.assertThat(savedTreatment.getName()).isEqualTo(treatmentName);
        Assertions.assertThat(savedTreatment.getPrice()).isEqualTo(treatmentPrice);
        Assertions.assertThat(savedTreatment.getLoyaltyPoints()).isEqualTo(treatmentLoyaltyPoints);
    }

    @Test
    public void givenAnAppointmentWithAPurchase_saveIt_thenRetrievedItFromDBTest() {
        Date startTime = new Date();
        Date endTime = new Date();

        String treatmentName = "Full Head Colour";
        double treatmentPrice = 85;
        long treatmentLoyaltyPoints = 20;

        Treatment treatment = new Treatment(treatmentName, treatmentPrice, treatmentLoyaltyPoints);

        Appointment appointment = new Appointment(client, startTime, endTime, Arrays.asList(treatment));


        String purchaseName = "Shampoo";
        double purchasePrice = 19.5;
        long purchaseLoyaltyPoints = 10;

        appointment.addPurchase(new Purchase(purchaseName, purchasePrice, purchaseLoyaltyPoints));

        appointmentRepository.save(appointment);


        List<Appointment> appointments = appointmentRepository.findAll();
        Assertions.assertThat(appointments).isNotEmpty();
        Assertions.assertThat(appointments).size().isEqualTo(1);

        Appointment savedAppointment = appointments.get(0);

        Assertions.assertThat(savedAppointment.getId()).isNotNull();
        Assertions.assertThat(savedAppointment.getClient()).isEqualTo(client);
        Assertions.assertThat(savedAppointment.getStartTime()).isEqualTo(startTime);
        Assertions.assertThat(savedAppointment.getEndTime()).isEqualTo(endTime);

        Assertions.assertThat(savedAppointment.getTreatments()).size().isEqualTo(1);
        Treatment savedTreatment = savedAppointment.getTreatments().get(0);

        Assertions.assertThat(savedTreatment.getName()).isEqualTo(treatmentName);
        Assertions.assertThat(savedTreatment.getPrice()).isEqualTo(treatmentPrice);
        Assertions.assertThat(savedTreatment.getLoyaltyPoints()).isEqualTo(treatmentLoyaltyPoints);

        Assertions.assertThat(savedAppointment.getPurchases()).size().isEqualTo(1);
        Purchase savedPurchase = savedAppointment.getPurchases().get(0);

        Assertions.assertThat(savedPurchase.getName()).isEqualTo(purchaseName);
        Assertions.assertThat(savedPurchase.getPrice()).isEqualTo(purchasePrice);
        Assertions.assertThat(savedPurchase.getLoyaltyPoints()).isEqualTo(purchaseLoyaltyPoints);
    }
}