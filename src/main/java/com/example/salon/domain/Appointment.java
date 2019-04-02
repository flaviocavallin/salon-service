package com.example.salon.domain;

import org.apache.commons.lang3.Validate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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

    private List<Treatment> treatments = new LinkedList<>();

    private List<Purchase> purchases = new LinkedList<>();

    Appointment() {
        //do nothing
    }

    public Appointment(Client client, Date startTime, Date endTime, List<Treatment> treatments) {
        this();
        this.client = Objects.requireNonNull(client, "client can not be null");
        this.startTime = Objects.requireNonNull(startTime, "startTime can not be null");
        this.endTime = Objects.requireNonNull(endTime, "endTime can not be null");
        this.treatments.addAll(treatments);
    }

    @Override
    public String getId() {
        return id;
    }

    protected void setId(String appointmentId){
        this.id = appointmentId;
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

    public void addPurchase(Purchase purchase) {
        Objects.requireNonNull(purchase, "purchase can not be null");
        purchases.add(purchase);
    }

    public void addAllPurchases(List<Purchase> purchases){
        Objects.requireNonNull(purchases, "purchases can not be null");
        this.purchases.addAll(purchases);
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    private void addTreatments(List<Treatment> treatments) {
        Validate.notEmpty(treatments, "treatments can not be null");
        this.treatments.addAll(treatments);
    }

    public void addService(Treatment treatment) {
        Objects.requireNonNull(treatment, "treatment can not be null");
        this.treatments.add(treatment);
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }
}
