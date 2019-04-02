package com.example.salon.repository;

import com.example.salon.domain.PointedClient;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CustomClientRepository {
    List<PointedClient> getTopMostLoyalActiveClientsBy(LocalDate dateFrom, LocalDate dateTo, int limit);

    void incrementLoyaltyPoints(String clientId, LocalDate date, long points);
}
