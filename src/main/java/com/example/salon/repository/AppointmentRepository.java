package com.example.salon.repository;

import com.example.salon.domain.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    //do nothing
}
