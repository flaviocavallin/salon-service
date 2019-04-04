package com.example.salon.service.impl;

import com.example.salon.domain.FileType;
import com.example.salon.service.ParserService;
import com.example.salon.service.parser.AppointmentCsvFileProcessor;
import com.example.salon.service.parser.ClientCsvFileProcessor;
import com.example.salon.service.parser.PurchaseCsvFileProcessor;
import com.example.salon.service.parser.TreatmentCsvFileProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;

public class CsvParserServiceTest {

    private ParserService parserService;

    private ClientCsvFileProcessor clientCsvFileProcessor = Mockito.mock(ClientCsvFileProcessor.class);
    private AppointmentCsvFileProcessor appointmentCsvFileProcessor = Mockito.mock(AppointmentCsvFileProcessor.class);
    private TreatmentCsvFileProcessor treatmentCsvFileProcessor = Mockito.mock(TreatmentCsvFileProcessor.class);
    private PurchaseCsvFileProcessor purchaseCsvFileProcessor = Mockito.mock(PurchaseCsvFileProcessor.class);


    @Before
    public void setUp() {
        parserService = new CsvParserService(clientCsvFileProcessor, appointmentCsvFileProcessor,
                treatmentCsvFileProcessor, purchaseCsvFileProcessor);
    }

    @Test
    public void processTest() {
        File file = Mockito.mock(File.class);

        parserService.process(FileType.CLIENTS, file);

        Mockito.verify(clientCsvFileProcessor).process(file);
    }
}
