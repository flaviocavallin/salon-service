package com.example.salon.listeners;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.context.ApplicationEvent;

public class LoyaltyPointEvent extends ApplicationEvent {

	private static final long serialVersionUID = -388536872752946035L;
	private UUID clientId;
    private LocalDate date;
    private long points;

    public LoyaltyPointEvent(Object source, UUID clientId, LocalDate date, long points) {
        super(source);
        this.clientId = clientId;
        this.date = date;
        this.points = points;
    }

    public UUID getClientId() {
        return clientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }
}
