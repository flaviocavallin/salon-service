package com.example.salon.service.impl;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.Client;
import com.example.salon.domain.LoyaltyPoint;
import com.example.salon.domain.Purchase;
import com.example.salon.domain.Treatment;
import com.example.salon.dto.PurchaseDTO;
import com.example.salon.dto.TreatmentDTO;
import com.example.salon.repository.AppointmentRepository;
import com.example.salon.repository.ClientRepository;
import com.example.salon.service.AppointmentService;
import com.example.salon.util.DateUtil;
import com.example.salon.util.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class AppointmentServiceImplIntegrationTest extends IntegrationTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;


    private Client client;
    private Appointment appointment;

    @Before
    public void setUp() {
        client = new Client("firstName1", "lastName1", "a1@a1.com", "phone", "Male");
        client = this.clientRepository.save(client);

        appointment = new Appointment(client, new Date(), new Date(), Lists.emptyList());

        appointment = appointmentRepository.save(appointment);
    }

    @Test
    public void given_treatmentDTO_then_push_into_Appointment_and_push_into_client_loyalty_points() {
        String name = "Full Head Colour";
        double price= 85;
        long loyaltyPoints= 1;

        TreatmentDTO treatmentDTO = new TreatmentDTO(name, price, loyaltyPoints);
        treatmentDTO.setAppointmentId(appointment.getId());

        Assertions.assertThat(appointment.getTreatments()).isEmpty();

        appointmentService.addTreatment(treatmentDTO);

        Appointment storedAppointment = appointmentRepository.findById(appointment.getId()).get();


        Assertions.assertThat(storedAppointment.getTreatments()).isNotEmpty();
        Assertions.assertThat(storedAppointment.getTreatments().size()).isEqualTo(1);

        Treatment treatment = storedAppointment.getTreatments().get(0);

        Assertions.assertThat(treatment.getName()).isEqualTo(name);
        Assertions.assertThat(treatment.getPrice()).isEqualTo(price);
        Assertions.assertThat(treatment.getLoyaltyPoints()).isEqualTo(loyaltyPoints);


        Client savedClient = clientRepository.findById(appointment.getClient().getId()).get();

        Assertions.assertThat(savedClient.getLoyaltyPoints()).size().isEqualTo(1);


        LoyaltyPoint loyaltyPoint = savedClient.getLoyaltyPoints().get(0);

        Assertions.assertThat(loyaltyPoint.getPoints()).isEqualTo(loyaltyPoints);
        Assertions.assertThat(loyaltyPoint.getDate()).isEqualTo(DateUtil.convertToLocaDate(appointment.getStartTime()));
    }


    @Test
    public void given_purchaseDTO_then_push_into_Appointment_and_push_into_client_loyalty_points() {
        String name = "Shampoo";
        double price= 19;
        long loyaltyPoints= 10;

        PurchaseDTO purchaseDTO = new PurchaseDTO(name, price, loyaltyPoints);
        purchaseDTO.setAppointmentId(appointment.getId());

        Assertions.assertThat(appointment.getTreatments()).isEmpty();

        appointmentService.addPurchase(purchaseDTO);

        Appointment storedAppointment = appointmentRepository.findById(appointment.getId()).get();


        Assertions.assertThat(storedAppointment.getPurchases()).isNotEmpty();
        Assertions.assertThat(storedAppointment.getPurchases().size()).isEqualTo(1);

        Purchase purchase = storedAppointment.getPurchases().get(0);

        Assertions.assertThat(purchase.getName()).isEqualTo(name);
        Assertions.assertThat(purchase.getPrice()).isEqualTo(price);
        Assertions.assertThat(purchase.getLoyaltyPoints()).isEqualTo(loyaltyPoints);


        Client savedClient = clientRepository.findById(appointment.getClient().getId()).get();

        Assertions.assertThat(savedClient.getLoyaltyPoints()).size().isEqualTo(1);


        LoyaltyPoint loyaltyPoint = savedClient.getLoyaltyPoints().get(0);

        Assertions.assertThat(loyaltyPoint.getPoints()).isEqualTo(loyaltyPoints);
        Assertions.assertThat(loyaltyPoint.getDate()).isEqualTo(DateUtil.convertToLocaDate(appointment.getStartTime()));
    }

}
