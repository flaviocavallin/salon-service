package com.example.salon.service;

import com.example.salon.dto.ClientDTO;

public interface ClientService {

    void create(ClientDTO clientDTO);

    ClientDTO getById(String clientId);

    void deleteById(String clientId);
}