package com.example.salon.service.impl;

import com.example.salon.domain.FileType;
import com.example.salon.service.CsvFileProcessor;
import com.example.salon.service.ParserService;
import com.example.salon.service.parser.AppointmentCsvFileProcessor;
import com.example.salon.service.parser.ClientCsvFileProcessor;
import com.example.salon.service.parser.PurchaseCsvFileProcessor;
import com.example.salon.service.parser.TreatmentCsvFileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Service
class CsvParserService implements ParserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvParserService.class);

    private final Map<FileType, CsvFileProcessor> map = new ConcurrentHashMap<>();

    CsvParserService(ClientCsvFileProcessor clientCsvFileProcessor,
                     AppointmentCsvFileProcessor appointmentCsvFileProcessor,
                     TreatmentCsvFileProcessor treatmentCsvFileProcessor,
                     PurchaseCsvFileProcessor purchaseCsvFileProcessor) {
        map.put(FileType.CLIENTS, clientCsvFileProcessor);
        map.put(FileType.APPOINTMENTS, appointmentCsvFileProcessor);
        map.put(FileType.SERVICES, treatmentCsvFileProcessor);
        map.put(FileType.PURCHASES, purchaseCsvFileProcessor);
    }

    @Override
    public void process(FileType fileType, File file) {
        Objects.requireNonNull(fileType, "fileType can not be null");
        Objects.requireNonNull(file, "file can not be null");

        CsvFileProcessor csvFileProcessor = map.get(fileType);
        if (csvFileProcessor == null) {
            throw new IllegalStateException("There is not csvProcessor for the fileType=" + fileType.getName());
        }
        csvFileProcessor.process(file);
    }
}
