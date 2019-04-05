package com.example.salon.repository;

import com.example.salon.domain.PointedClient;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CustomClientRepository {
    List<PointedClient> getTopMostLoyalActiveClientsBy(LocalDate dateFrom, LocalDate dateTo, int limit);

    void incrementLoyaltyPoints(UUID clientId, LocalDate date, long points);

}
