package com.example.salon.dto;

import java.util.Objects;
import java.util.UUID;

public class PointedClientDTO {
    private UUID id;
    private String email;
    private long points;

    PointedClientDTO() {
        //do nothing
    }

    public PointedClientDTO(UUID id, String email, long points) {
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
}
