package com.example.salon.domain;

import java.time.LocalDate;
import java.util.Objects;

public class LoyaltyPoint extends AbstractDocument<LocalDate> {

	private static final long serialVersionUID = -8084659654368252200L;
	private LocalDate date;
    private long points;

    LoyaltyPoint() {
        //do nothing
    }

    public LoyaltyPoint(LocalDate date, long points) {
        this.date = Objects.requireNonNull(date, "date can not be null");
        this.points = points;
    }

    public void incrementPoints(long points) {
        this.points = this.points + points;
    }

    @Override
    public LocalDate getId() {
        return getDate();
    }

    public LocalDate getDate() {
        return date;
    }

    public long getPoints() {
        return points;
    }

}
