package com.example.salon.service.impl;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.Client;
import com.example.salon.domain.Purchase;
import com.example.salon.domain.Treatment;
import com.example.salon.dto.AppointmentDTO;
import com.example.salon.dto.PurchaseDTO;
import com.example.salon.exceptions.EntityNotFoundException;
import com.example.salon.repository.AppointmentRepository;
import com.example.salon.repository.ClientRepository;
import com.example.salon.service.AppointmentService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;

    AppointmentServiceImpl(AppointmentRepository appointmentRepository, ClientRepository clientRepository) {
        this.appointmentRepository = Objects.requireNonNull(appointmentRepository, "appointmentRepository can not be " +
                "null");
        this.clientRepository = Objects.requireNonNull(clientRepository, "clientRepository can not be null");
    }

    @Override
    public void create(AppointmentDTO appointmentDTO) {
        Objects.requireNonNull(appointmentDTO, "appointmentDTO can not be null");

        Client client =
                clientRepository.findById(appointmentDTO.getClientId()).orElseThrow(() -> new EntityNotFoundException("The " +
                        "email does not exists"));


        List<Treatment> treatments = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(appointmentDTO.getTreatments())) {
            treatments = appointmentDTO.getTreatments().stream().map(t -> new Treatment(t.getName(), t.getPrice(),
                    t.getLoyaltyPoints())).collect(Collectors.toList());
        }

        Appointment appointment = new Appointment(client, appointmentDTO.getStartTime(), appointmentDTO.getEndTime(),
                treatments);

        appointmentRepository.save(appointment);
    }

    @Override
    public void addPurchase(String appointmentId, PurchaseDTO purchaseDTO) {
        Objects.requireNonNull(appointmentId, "appointmentId can not be null");
        Objects.requireNonNull(purchaseDTO, "purchaseDTO can not be null");

        Appointment appointment =
                appointmentRepository.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("The " +
                        "appointment does not exists"));

        Purchase purchase = new Purchase(purchaseDTO.getName(), purchaseDTO.getPrice(),
                purchaseDTO.getLoyaltyPoints());

        appointment.addPurchase(purchase);

        appointmentRepository.save(appointment);
    }
}
