package com.example.salon.listeners;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

import com.example.salon.domain.Appointment;
import com.example.salon.factory.LoyaltyPointEventFactory;
import com.example.salon.service.AppointmentService;

@Component
public class AppointmentEventListener extends AbstractMongoEventListener<Appointment> {

	private ApplicationEventPublisher applicationEventPublisher;
	private final AppointmentService appointmentService;
	private final LoyaltyPointEventFactory loyaltyPointEventFactory;
	private final Map<UUID, Appointment> map;

	AppointmentEventListener(ApplicationEventPublisher applicationEventPublisher, AppointmentService appointmentService,
			LoyaltyPointEventFactory loyaltyPointEventFactory) {
		this.applicationEventPublisher = Objects.requireNonNull(applicationEventPublisher,
				"clientService can not be null");
		this.appointmentService = Objects.requireNonNull(appointmentService, "appointmentService can not be null");
		this.loyaltyPointEventFactory = Objects.requireNonNull(loyaltyPointEventFactory,
				"loyaltyPointEventFactory " + "can not be null");
		this.map = new ConcurrentHashMap<>();
	}

	@Override
	public void onAfterSave(AfterSaveEvent<Appointment> event) {
		LoyaltyPointEvent loyaltyPointEvent = loyaltyPointEventFactory.convert(event.getSource());
		applicationEventPublisher.publishEvent(loyaltyPointEvent);
	}

	@Override
	public void onBeforeDelete(BeforeDeleteEvent<Appointment> event) {
		UUID appointmentId = (UUID) event.getSource().get("_id");
		Optional<Appointment> opAppointment = appointmentService.findById(appointmentId);
		if (opAppointment.isPresent()) {
			map.put(appointmentId, opAppointment.get());
		}
	}

	@Override
	public void onAfterDelete(AfterDeleteEvent<Appointment> event) {
		UUID appointmentId = (UUID) event.getSource().get("_id");
		Appointment appointment = map.remove(appointmentId);

		if (appointment != null) {
			LoyaltyPointEvent loyaltyPointEvent = loyaltyPointEventFactory.convert(appointment);
			loyaltyPointEvent.setPoints(-loyaltyPointEvent.getPoints());
			applicationEventPublisher.publishEvent(loyaltyPointEvent);
		}
	}
}