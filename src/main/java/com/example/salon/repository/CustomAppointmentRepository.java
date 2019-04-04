package com.example.salon.repository;

import com.example.salon.domain.Purchase;
import com.example.salon.domain.Treatment;

import java.util.UUID;

public interface CustomAppointmentRepository {

    boolean addTreatment(UUID appointmentId, Treatment treatment);

    boolean addPurchase(UUID appointmentId, Purchase purchase);
}

