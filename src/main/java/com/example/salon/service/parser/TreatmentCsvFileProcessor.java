package com.example.salon.service.parser;

import com.example.salon.dto.TreatmentDTO;
import com.example.salon.service.CsvFileProcessor;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.util.CheckedConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Component
public class TreatmentCsvFileProcessor implements CsvFileProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreatmentCsvFileProcessor.class);

    private final CheckedConsumer<TreatmentDTO> consumer;

    TreatmentCsvFileProcessor(@Qualifier("treatmentConsumer") CheckedConsumer<TreatmentDTO> consumer) {
        this.consumer = Objects.requireNonNull(consumer, "consumer can not be null");
    }

    @Override
    public void process(File file) {
        try {
            CsvParser
                    .skip(1)
                    .parallelReader()
                    .mapTo(TreatmentDTO.class)
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