package com.example.salon.service;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.PointedClient;
import com.example.salon.dto.ClientDTO;
import com.example.salon.dto.PointedClientDTO;
import com.example.salon.listeners.LoyaltyPointEvent;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ClientService {

    void create(ClientDTO clientDTO);

    ClientDTO getById(String clientId);

    void deleteById(String clientId);

    void incrementClientLoyaltyPoints(LoyaltyPointEvent loyaltyPointEvent);

    void decrementClientLoyaltyPoints(LoyaltyPointEvent loyaltyPointEvent);

    List<PointedClientDTO> getTopMostLoyalActiveClientsBy(LocalDate dateFrom, LocalDate dateTo, int limit);
}