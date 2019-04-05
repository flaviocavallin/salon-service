package com.example.salon.service;

import com.example.salon.dto.ClientDTO;
import com.example.salon.dto.PointedClientDTO;
import com.example.salon.listeners.LoyaltyPointEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ClientService {

    ClientDTO save(ClientDTO clientDTO);

    ClientDTO getById(UUID clientId);

    void deleteById(UUID clientId);

    void incrementClientLoyaltyPoints(LoyaltyPointEvent loyaltyPointEvent);

    List<PointedClientDTO> getTopMostLoyalActiveClientsBy(LocalDate dateFrom, LocalDate dateTo, int limit);

    void banClient(UUID clientId);
}