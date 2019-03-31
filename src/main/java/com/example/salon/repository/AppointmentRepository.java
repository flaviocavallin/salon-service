package com.example.salon.repository;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    boolean existsByClient_Id(String clientId);
}
