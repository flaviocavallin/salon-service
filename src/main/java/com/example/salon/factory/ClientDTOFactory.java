package com.example.salon.factory;

import com.example.salon.domain.Client;
import com.example.salon.dto.ClientDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ClientDTOFactory {

    public ClientDTOFactory() {
        //do nothing
    }

    public Client fromDTO(ClientDTO clientDTO) {
        Objects.requireNonNull(clientDTO, "clientDTO can not be null");

        Client client = new Client(clientDTO.getId(), clientDTO.getFirstName(), clientDTO.getLastName(), clientDTO.getEmail(),
                clientDTO.getPhone(), clientDTO.getGender(),clientDTO.getBanned());

        return client;
    }

    public ClientDTO toDTO(Client client) {
        Objects.requireNonNull(client, "client can not be null");

        ClientDTO clientDTO = new ClientDTO(client.getFirstName(), client.getLastName(), client.getEmail(),
                client.getPhone(), client.getGender());
        clientDTO.setId(client.getId());
        clientDTO.setBanned(client.isBanned());

        return clientDTO;
    }
}
