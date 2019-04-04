package com.example.salon.service.parser;

import com.example.salon.dto.AppointmentDTO;
import com.example.salon.service.AppointmentService;
import org.simpleflatmapper.util.CheckedConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
class AppointmentConsumer implements CheckedConsumer<AppointmentDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentConsumer.class);

    private final AppointmentService appointmentService;

    AppointmentConsumer(AppointmentService appointmentService) {
        this.appointmentService = Objects.requireNonNull(appointmentService, "appointmentService can not be null");
    }

    @Override
    public void accept(AppointmentDTO appointmentDTO) throws Exception {
        try {
            appointmentService.save(appointmentDTO);
        } catch (Exception e) {
            LOGGER.error("Row discarded={}", appointmentDTO, e);
            //TODO add some strategy to save discarded rows
        }
    }

}
