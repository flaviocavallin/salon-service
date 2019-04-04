package com.example.salon.service.parser;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.util.CheckedConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.salon.dto.PurchaseDTO;
import com.example.salon.service.CsvFileProcessor;

@Component
public class PurchaseCsvFileProcessor implements CsvFileProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseCsvFileProcessor.class);

    private final CheckedConsumer<PurchaseDTO> consumer;

    PurchaseCsvFileProcessor(@Qualifier("purchaseConsumer") CheckedConsumer<PurchaseDTO> consumer) {
        this.consumer = Objects.requireNonNull(consumer, "consumer can not be null");
    }

    @Override
    public void process(File file) {
        try {
            CsvParser
                    .skip(1)
                    .parallelReader()
                    .mapTo(PurchaseDTO.class)
                    .alias("appointment_id", "appointmentId")
                    .alias("loyalty_points", "loyaltyPoints")
                    .headers("id", "appointment_id", "name", "price", "loyalty_points")
                    .forEach(file, consumer);
        } catch (IOException e) {
            LOGGER.error("There was an issue trying to parse file={}", file.getName(), e);
            throw new CsvParserException("There was an issue trying to parse file=" + file.getName());
        }
    }

}