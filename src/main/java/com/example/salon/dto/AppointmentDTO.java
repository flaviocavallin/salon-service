package com.example.salon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AppointmentDTO {

    private UUID appointmentId;

    private UUID clientId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Date startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Date endTime;

    private List<TreatmentDTO> treatments;

    private List<PurchaseDTO> purchases;

    AppointmentDTO() {
        //do nothing
    }

    public AppointmentDTO(UUID clientId, Date startTime, Date endTime) {
        this();
        this.clientId = Objects.requireNonNull(clientId, "clientId can not be null");
        this.startTime = Objects.requireNonNull(startTime, "startTime can not be null");
        this.endTime = Objects.requireNonNull(endTime, "endTime can not be null");
    }

    public AppointmentDTO(UUID clientId, Date startTime, Date endTime,
                          List<TreatmentDTO> treatments) {
        this(clientId, startTime, endTime);
        this.treatments = Objects.requireNonNull(treatments, "treatments can not be null");
    }

    public UUID getClientId() {
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

    public List<PurchaseDTO> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseDTO> purchases) {
        this.purchases = purchases;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(UUID appointmentId) {
        this.appointmentId = appointmentId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
