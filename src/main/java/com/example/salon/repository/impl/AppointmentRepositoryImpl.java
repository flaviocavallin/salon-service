package com.example.salon.repository.impl;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.Purchase;
import com.example.salon.domain.Treatment;
import com.example.salon.repository.CustomAppointmentRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.UUID;

@Repository
class AppointmentRepositoryImpl implements CustomAppointmentRepository {

    private final MongoTemplate mongoTemplate;

    AppointmentRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = Objects.requireNonNull(mongoTemplate, "mongoTemplate can not be null");
    }

    @Override
    public boolean addTreatment(UUID appointmentId, Treatment treatment) {
        Objects.requireNonNull(appointmentId, "appointmentId can not be null");
        Objects.requireNonNull(treatment, "treatment can not be null");

        Query query = new Query().addCriteria(Criteria.where("_id").is(appointmentId));

        Update update = new Update().addToSet("treatments", treatment);

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Appointment.class);

        return updateResult.getModifiedCount() > 0;
    }

    @Override
    public boolean addPurchase(UUID appointmentId, Purchase purchase) {
        Objects.requireNonNull(appointmentId, "appointmentId can not be null");
        Objects.requireNonNull(purchase, "purchase can not be null");

        Query query = new Query().addCriteria(Criteria.where("_id").is(appointmentId));

        Update update = new Update().addToSet("purchases", purchase);

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Appointment.class);

        return updateResult.getModifiedCount() > 0;
    }
}
