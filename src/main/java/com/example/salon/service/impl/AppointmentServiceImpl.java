package com.example.salon.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.Client;
import com.example.salon.domain.Purchase;
import com.example.salon.domain.Treatment;
import com.example.salon.dto.AppointmentDTO;
import com.example.salon.dto.PurchaseDTO;
import com.example.salon.dto.TreatmentDTO;
import com.example.salon.exceptions.EntityNotFoundException;
import com.example.salon.listeners.LoyaltyPointEvent;
import com.example.salon.repository.AppointmentRepository;
import com.example.salon.repository.ClientRepository;
import com.example.salon.service.AppointmentService;
import com.example.salon.util.DateUtil;

@Service
class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final ApplicationEventPublisher applicationEventPublisher;


    AppointmentServiceImpl(AppointmentRepository appointmentRepository, ClientRepository clientRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.appointmentRepository = Objects.requireNonNull(appointmentRepository, "appointmentRepository can not be " +
                "null");
        this.clientRepository = Objects.requireNonNull(clientRepository, "clientRepository can not be null");
        this.applicationEventPublisher = Objects.requireNonNull(applicationEventPublisher, "applicationEventPublisher can not be null");
    }

    @Override
    public void save(AppointmentDTO appointmentDTO) {
        Objects.requireNonNull(appointmentDTO, "appointmentDTO can not be null");

        Client client =
                clientRepository.findById(appointmentDTO.getClientId()).orElseThrow(() -> new EntityNotFoundException("The " +
                        "clientId does not exists"));


        List<Treatment> treatments = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(appointmentDTO.getTreatments())) {
            treatments = appointmentDTO.getTreatments().stream().map(t -> new Treatment(t.getName(), t.getPrice(),
                    t.getLoyaltyPoints())).collect(Collectors.toList());
        }

        Appointment appointment = new Appointment(appointmentDTO.getAppointmentId(), client,
                appointmentDTO.getStartTime(), appointmentDTO.getEndTime(),
                treatments);

        if (CollectionUtils.isNotEmpty(appointmentDTO.getPurchases())) {
            List<Purchase> purchases = appointmentDTO.getPurchases().stream().map(t -> new Purchase(t.getName(),
                    t.getPrice(),
                    t.getLoyaltyPoints())).collect(Collectors.toList());
            appointment.addAllPurchases(purchases);
        }

        appointmentRepository.save(appointment);
    }

    @Override
    public void deleteById(UUID appointmentId) {
        Objects.requireNonNull(appointmentId, "appointmentId can not be null");

        appointmentRepository.deleteById(appointmentId);
    }

    @Override
    public Optional<Appointment> findById(UUID appointmentId) {
        Objects.requireNonNull(appointmentId, "appointmentId can not be null");

        return appointmentRepository.findById(appointmentId);
    }

    @Override
    public void addTreatment(TreatmentDTO treatmentDTO) {
        Objects.requireNonNull(treatmentDTO, "treatmentDTO can not be null");

        Treatment treatment = new Treatment(treatmentDTO.getId(), treatmentDTO.getName(),
                treatmentDTO.getPrice(), treatmentDTO.getLoyaltyPoints());

        boolean added = appointmentRepository.addTreatment(treatmentDTO.getAppointmentId(), treatment);

        if (added) {
            Appointment appointment =
                    appointmentRepository.findById(treatmentDTO.getAppointmentId()).orElseThrow(() -> new EntityNotFoundException("The " +
                            "appointment does not exists"));


            LoyaltyPointEvent loyaltyPointEvent = new LoyaltyPointEvent(this, appointment.getClient().getId(),
                    DateUtil.convertToLocaDate(appointment.getStartTime()), treatment.getLoyaltyPoints());
            applicationEventPublisher.publishEvent(loyaltyPointEvent);
        }
    }

    @Override
    public void addPurchase(PurchaseDTO purchaseDTO) {
        Objects.requireNonNull(purchaseDTO, "purchaseDTO can not be null");

        Purchase purchase = new Purchase(purchaseDTO.getId(), purchaseDTO.getName(),
                purchaseDTO.getPrice(), purchaseDTO.getLoyaltyPoints());

        boolean added = appointmentRepository.addPurchase(purchaseDTO.getAppointmentId(), purchase);

        if (added) {
            Appointment appointment =
                    appointmentRepository.findById(purchaseDTO.getAppointmentId()).orElseThrow(() -> new EntityNotFoundException("The " +
                            "appointment does not exists"));

            LoyaltyPointEvent loyaltyPointEvent = new LoyaltyPointEvent(this, appointment.getClient().getId(),
                    DateUtil.convertToLocaDate(appointment.getStartTime()), purchase.getLoyaltyPoints());
            applicationEventPublisher.publishEvent(loyaltyPointEvent);
        }
    }

}
