package com.example.salon.listeners;

import java.time.LocalDate;
import java.util.Objects;

public class LoyaltyPointEvent {

    private String clientId;
    private LocalDate date;
    private long points;

    public LoyaltyPointEvent(String clientId, LocalDate date, long points) {
        this.clientId = Objects.requireNonNull(clientId, "clientId can not be null");
        this.date = Objects.requireNonNull(date, "date can not be null");
        this.points = points;
    }

    public String getClientId() {
        return clientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getPoints() {
        return points;
    }
}
