package com.example.salon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AppointmentDTO {

    private String clientId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Date endTime;

    private List<TreatmentDTO> treatments;

    AppointmentDTO() {
        //do nothing
    }

    public AppointmentDTO(String clientId, Date startTime, Date endTime, List<TreatmentDTO> treatments) {
        this.clientId = Objects.requireNonNull(clientId, "clientId can not be null");
        this.startTime = Objects.requireNonNull(startTime, "startTime can not be null");
        this.endTime = Objects.requireNonNull(endTime, "endTime can not be null");
        this.treatments = Objects.requireNonNull(treatments, "treatments can not be null");
    }

    public String getClientId() {
        return clientId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public List<TreatmentDTO> getTreatments() {
        return treatments;
    }
}
