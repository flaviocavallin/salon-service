package com.example.salon.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;
import java.util.UUID;

public class PointedClient {

    private UUID id;
    private String email;
    private long points;

    PointedClient() {
        //do nothing
    }

    public PointedClient(UUID id, String email, long points) {
        this.id = Objects.requireNonNull(id, "id can not be null");
        this.email = Objects.requireNonNull(email, "email can not be null");
        this.points = points;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public long getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}