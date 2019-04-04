package com.example.salon.service.parser;

import com.example.salon.dto.ClientDTO;
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
public class ClientCsvFileProcessor implements CsvFileProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientCsvFileProcessor.class);

    private final CheckedConsumer<ClientDTO> consumer;

    ClientCsvFileProcessor(@Qualifier("clientConsumer") CheckedConsumer<ClientDTO> consumer) {
        this.consumer = Objects.requireNonNull(consumer, "consumer can not be null");
    }

    @Override
    public void process(File file) {
        try {
            CsvParser
                    .skip(1)
                    .parallelReader()
                    .mapTo(ClientDTO.class)
                    .alias("first_name", "firstName")
                    .alias("last_name", "lastName")
                    .headers("id", "first_name", "last_name", "email", "phone", "gender", "banned")
                    .forEach(file, consumer);
        } catch (IOException e) {
            LOGGER.error("There was an issue trying to parse file={}", file.getName(), e);
            throw new CsvParserException("There was an issue trying to parse file=" + file.getName());
        }
    }

}
