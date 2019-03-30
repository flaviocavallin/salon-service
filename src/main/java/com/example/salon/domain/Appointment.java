package com.example.salon.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Document(collection = "appointments")
@TypeAlias("appointment")
public class Appointment extends AbstractDocument<String> {

    @Id
    private String id;

    @DBRef(lazy = true)
    private Client client;

    private Date startTime;
    private Date endTime;

    Appointment() {
        //do nothing
    }

    public Appointment(Client client, Date startTime, Date endTime) {
        this();
        this.client = Objects.requireNonNull(client, "client can not be null");
        this.startTime = Objects.requireNonNull(startTime, "startTime can not be null");
        this.endTime = Objects.requireNonNull(endTime, "endTime can not be null");
    }
    
    @Override
    public String getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

}
