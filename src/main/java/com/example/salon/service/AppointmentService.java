package com.example.salon.service;

import com.example.salon.dto.AppointmentDTO;
import com.example.salon.dto.PurchaseDTO;

public interface AppointmentService {

    void create(AppointmentDTO appointmentDTO);

    void addPurchase(String appointmentId, PurchaseDTO purchaseDTO);
}
