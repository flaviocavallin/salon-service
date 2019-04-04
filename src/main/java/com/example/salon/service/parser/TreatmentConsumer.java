package com.example.salon.service.parser;

import java.util.Objects;

import org.simpleflatmapper.util.CheckedConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.salon.dto.TreatmentDTO;
import com.example.salon.service.AppointmentService;

@Component
class TreatmentConsumer implements CheckedConsumer<TreatmentDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreatmentConsumer.class);

    private final AppointmentService appointmentService;

    TreatmentConsumer(AppointmentService appointmentService) {
        this.appointmentService = Objects.requireNonNull(appointmentService, "appointmentService can not be null");
    }

    @Override
    public void accept(TreatmentDTO treatmentDTO) throws Exception {
        try {
            appointmentService.addTreatment(treatmentDTO);
        } catch (Exception e) {
            LOGGER.error("Row discarded={}", treatmentDTO, e);
            //TODO add some strategy to save discarded rows
        }
    }

}
