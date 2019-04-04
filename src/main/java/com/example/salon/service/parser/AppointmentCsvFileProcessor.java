package com.example.salon.service.parser;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.simpleflatmapper.csv.CsvColumnDefinition;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.util.CheckedConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.salon.dto.AppointmentDTO;
import com.example.salon.service.CsvFileProcessor;

@Component
public class AppointmentCsvFileProcessor implements CsvFileProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentCsvFileProcessor.class);

    private final CheckedConsumer<AppointmentDTO> consumer;

    AppointmentCsvFileProcessor(@Qualifier("appointmentConsumer") CheckedConsumer<AppointmentDTO> consumer) {
        this.consumer = Objects.requireNonNull(consumer, "consumer can not be null");
    }

    @Override
    public void process(File file) {
        try {
            CsvParser
                    .skip(1)
                    .parallelReader()
                    .mapTo(AppointmentDTO.class)
                    .columnDefinition("start_time", CsvColumnDefinition.dateFormatDefinition("yyyy-MM-dd HH:mm:ss Z"))
                    .columnDefinition("end_time", CsvColumnDefinition.dateFormatDefinition("yyyy-MM-dd HH:mm:ss Z"))
                    .alias("id", "appointmentId")
                    .alias("client_id", "clientId")
                    .alias("start_time", "startTime")
                    .alias("end_time", "endTime")
                    .headers("id", "client_id", "start_time", "end_time")
                    .forEach(file, consumer);
        } catch (IOException e) {
            LOGGER.error("There was an issue trying to parse file={}", file.getName(), e);
            throw new CsvParserException("There was an issue trying to parse file=" + file.getName());
        }
    }

}