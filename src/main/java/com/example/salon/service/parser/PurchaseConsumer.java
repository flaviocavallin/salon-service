package com.example.salon.service.parser;

import java.util.Objects;

import org.simpleflatmapper.util.CheckedConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.salon.dto.PurchaseDTO;
import com.example.salon.service.AppointmentService;

@Component
class PurchaseConsumer implements CheckedConsumer<PurchaseDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseConsumer.class);

    private final AppointmentService appointmentService;

    PurchaseConsumer(AppointmentService appointmentService) {
        this.appointmentService = Objects.requireNonNull(appointmentService, "appointmentService can not be null");
    }

    @Override
    public void accept(PurchaseDTO purchaseDTO) throws Exception {
        try {
            appointmentService.addPurchase(purchaseDTO);
        } catch (Exception e) {
            LOGGER.error("Row discarded={}", purchaseDTO, e);
            //TODO add some strategy to save discarded rows
        }
    }

}
