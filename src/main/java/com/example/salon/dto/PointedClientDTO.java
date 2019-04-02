package com.example.salon.dto;

import java.util.Objects;

public class PointedClientDTO {
    private String id;
    private String email;
    private long points;

    PointedClientDTO() {
        //do nothing
    }

    public PointedClientDTO(String id, String email, long points) {
        this.id = Objects.requireNonNull(id, "id can not be null");
        this.email = Objects.requireNonNull(email, "email can not be null");
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public long getPoints() {
        return points;
    }
}
