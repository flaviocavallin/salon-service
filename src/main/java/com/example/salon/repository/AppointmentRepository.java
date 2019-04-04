package com.example.salon.repository;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, UUID>, CustomAppointmentRepository {
    boolean existsByClient_Id(UUID clientId);
}
