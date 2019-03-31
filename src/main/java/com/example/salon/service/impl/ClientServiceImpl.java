package com.example.salon.service.impl;

import com.example.salon.domain.Client;
import com.example.salon.dto.ClientDTO;
import com.example.salon.exceptions.EntityNotFoundException;
import com.example.salon.factory.ClientDTOFactory;
import com.example.salon.repository.ClientRepository;
import com.example.salon.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientDTOFactory clientDTOFactory;

    ClientServiceImpl(ClientRepository clientRepository, ClientDTOFactory clientDTOFactory) {
        this.clientRepository = Objects.requireNonNull(clientRepository, "clientRepository can not be null");
        this.clientDTOFactory = Objects.requireNonNull(clientDTOFactory, "clientDTOFactory can not be null");
    }

    @Override
    public void create(ClientDTO clientDTO) {
        Objects.requireNonNull(clientDTO, "clientDTO can not be null");

        Client client = clientDTOFactory.fromDTO(clientDTO);

        clientRepository.save(client);
    }

    @Override
    public ClientDTO getById(String clientId) {
        Objects.requireNonNull(clientId, "clientId can not be null");

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException("The " +
                "clientId does not exists"));

        return clientDTOFactory.toDTO(client);
    }
}
