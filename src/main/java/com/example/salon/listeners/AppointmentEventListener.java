package com.example.salon.listeners;

import com.example.salon.domain.Appointment;
import com.example.salon.factory.LoyaltyPointEventFactory;
import com.example.salon.service.AppointmentService;
import com.example.salon.service.ClientService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AppointmentEventListener extends AbstractMongoEventListener<Appointment> {

    private final ClientService clientService;
    private final AppointmentService appointmentService;
    private final LoyaltyPointEventFactory loyaltyPointEventFactory;
    private final Map<String, Appointment> map;

    AppointmentEventListener(ClientService clientService, AppointmentService appointmentService,
                             LoyaltyPointEventFactory loyaltyPointEventFactory) {
        this.clientService = Objects.requireNonNull(clientService, "clientService can not be null");
        this.appointmentService = Objects.requireNonNull(appointmentService, "appointmentService can not be null");
        this.loyaltyPointEventFactory = Objects.requireNonNull(loyaltyPointEventFactory, "loyaltyPointEventFactory " +
                "can not be null");
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Appointment> event) {
        LoyaltyPointEvent loyaltyPointEvent = loyaltyPointEventFactory.convert(event.getSource());
        clientService.incrementClientLoyaltyPoints(loyaltyPointEvent);
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Appointment> event) {
        String appointmentId = event.getSource().getString("_id");
        Optional<Appointment> opAppointment = appointmentService.findById(appointmentId);
        if (opAppointment.isPresent()) {
            map.put(appointmentId, opAppointment.get());
        }
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Appointment> event) {
        String appointmentId = event.getSource().getString("_id");
        Appointment appointment = map.remove(appointmentId);

        if (appointment != null) {
            LoyaltyPointEvent loyaltyPointEvent = loyaltyPointEventFactory.convert(appointment);
            clientService.decrementClientLoyaltyPoints(loyaltyPointEvent);
        }
    }
}