package com.example.salon.listeners;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.Client;
import com.example.salon.domain.Treatment;
import com.example.salon.factory.LoyaltyPointEventFactory;
import com.example.salon.service.AppointmentService;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppointmentEventListenerTest {

    private AppointmentEventListener listener;

    private ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private AppointmentService appointmentService = mock(AppointmentService.class);
    private LoyaltyPointEventFactory loyaltyPointEventFactory = mock(LoyaltyPointEventFactory.class);

    private Appointment appointment;
    private UUID appointmentId = UUID.randomUUID();

    private UUID clientId = UUID.randomUUID();
    private LocalDate date = LocalDate.now();
    private long points = 10;

    @Before
    public void setUp() {
        this.listener = new AppointmentEventListener(applicationEventPublisher, appointmentService,
                loyaltyPointEventFactory);

        appointment = new Appointment(mock(Client.class), mock(Date.class), mock(Date.class),
                Arrays.asList(mock(Treatment.class))) {
            @Override
            protected void setId(UUID appointmentId) {
                super.setId(appointmentId);
            }
        };
    }

    @Test
    public void given_AfterSaveAppointmentEvent_then_call_IncrementClientLoyaltyPoints() {
        AfterSaveEvent<Appointment> event = mock(AfterSaveEvent.class);

        when(event.getSource()).thenReturn(appointment);

        LoyaltyPointEvent loyaltyPointEvent = new LoyaltyPointEvent(this, clientId, date, points);
        when(loyaltyPointEventFactory.convert(appointment)).thenReturn(loyaltyPointEvent);

        this.listener.onAfterSave(event);

        verify(applicationEventPublisher).publishEvent(loyaltyPointEvent);
        verify(event).getSource();
    }


    @Test
    public void given_BeforeDeleteEventAppointment_then_save_appointment_then_call_DecrementClientLoyaltyPoints_on_After_DeleteEvent() {
        BeforeDeleteEvent<Appointment> beforeDeleteEvent = mock(BeforeDeleteEvent.class);

        Document document = mock(Document.class);
        when(document.get("_id")).thenReturn(appointmentId);
        when(beforeDeleteEvent.getSource()).thenReturn(document);

        when(appointmentService.findById(appointmentId)).thenReturn(Optional.of(appointment));

        this.listener.onBeforeDelete(beforeDeleteEvent);
        verify(appointmentService).findById(appointmentId);


        AfterDeleteEvent<Appointment> afterDeleteEvent = mock(AfterDeleteEvent.class);
        when(afterDeleteEvent.getSource()).thenReturn(document);

        LoyaltyPointEvent loyaltyPointEvent = new LoyaltyPointEvent(this, clientId, date, points);
        when(loyaltyPointEventFactory.convert(appointment)).thenReturn(loyaltyPointEvent);


        this.listener.onAfterDelete(afterDeleteEvent);

        verify(applicationEventPublisher).publishEvent(loyaltyPointEvent);
        verify(loyaltyPointEventFactory).convert(appointment);
    }
}