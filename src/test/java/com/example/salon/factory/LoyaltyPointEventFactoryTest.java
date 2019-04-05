package com.example.salon.factory;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.Client;
import com.example.salon.domain.Purchase;
import com.example.salon.domain.Treatment;
import com.example.salon.listeners.LoyaltyPointEvent;
import com.example.salon.util.DateUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class LoyaltyPointEventFactoryTest {

    private LoyaltyPointEventFactory factory;

    private Appointment appointment;
    private UUID clientId = UUID.randomUUID();

    private String treatmentName = "Pedicure";
    private double treatmentPrice = 40;
    private long treatmentLoyaltyPoints = 20;

    private String purchaseName = "Body Wash";
    private double purchasePrice = 11;
    private long purchaseLoyaltyPoints = 10;

    private Date startTime = new Date();

    @Before
    public void setUp() {
        this.factory = new LoyaltyPointEventFactory();

        String firstName = "firstName1";
        String lastName = "lastName1";
        String email = "a1@a1.com";
        String phone = "123";
        String gender = "Male";

        Client client = new Client(firstName, lastName, email, phone, gender) {
            @Override
            public UUID getId() {
                return clientId;
            }
        };

        Date endTime = new Date();
        Treatment treatment = new Treatment(treatmentName, treatmentPrice, treatmentLoyaltyPoints);


        appointment = new Appointment(client, startTime, endTime, Arrays.asList(treatment));

        Purchase purchase = new Purchase(purchaseName, purchasePrice, purchaseLoyaltyPoints);
        appointment.addPurchase(purchase);
    }


    @Test
    public void given_Appointment_then_convert_LoyaltyPointEvent() {
        LoyaltyPointEvent loyaltyPointEvent = this.factory.convert(appointment);

        Assertions.assertThat(loyaltyPointEvent.getClientId()).isEqualTo(clientId);
        Assertions.assertThat(loyaltyPointEvent.getDate()).isEqualTo(DateUtil.convertToLocaDate(startTime));
        Assertions.assertThat(loyaltyPointEvent.getPoints()).isEqualTo(treatmentLoyaltyPoints + purchaseLoyaltyPoints);
    }

}
