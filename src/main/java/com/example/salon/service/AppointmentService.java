package com.example.salon.service;

import com.example.salon.domain.Appointment;
import com.example.salon.dto.AppointmentDTO;
import com.example.salon.dto.PurchaseDTO;
import com.example.salon.dto.TreatmentDTO;

import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {

    void save(AppointmentDTO appointmentDTO);

    void deleteById(UUID appointmentId);

    Optional<Appointment> findById(UUID appointmentId);

    void addTreatment(TreatmentDTO treatmentDTO);

    void addPurchase(PurchaseDTO purchaseDTO);

}
