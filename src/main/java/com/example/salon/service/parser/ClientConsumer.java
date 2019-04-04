package com.example.salon.service.parser;

import java.util.Objects;

import org.simpleflatmapper.util.CheckedConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.salon.dto.ClientDTO;
import com.example.salon.service.ClientService;

@Component
class ClientConsumer implements CheckedConsumer<ClientDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConsumer.class);

    private final ClientService clientService;

    ClientConsumer(ClientService clientService) {
        this.clientService = Objects.requireNonNull(clientService, "clientService can not be null");
    }

    @Override
    public void accept(ClientDTO parserClientDTO) throws Exception {
        try {
            clientService.save(parserClientDTO);
        } catch (Exception e) {
            LOGGER.error("Row discarded={}", parserClientDTO, e);
            //TODO add some strategy to save discarded rows
        }
    }

}
