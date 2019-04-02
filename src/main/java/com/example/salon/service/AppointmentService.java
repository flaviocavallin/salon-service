package com.example.salon.service;

import com.example.salon.domain.Appointment;
import com.example.salon.dto.AppointmentDTO;

import java.util.Optional;

public interface AppointmentService {

    void create(AppointmentDTO appointmentDTO);

    void deleteById(String appointmentId);

    Optional<Appointment> findById(String appointmentId);
}
