package com.example.salon.domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = Appointment.COLLECTION_NAME)
@TypeAlias("appointment")
public class Appointment extends AbstractDocument<UUID> {

	private static final long serialVersionUID = -7199261031041360470L;

	public static final String COLLECTION_NAME = "appointments";

    @Id
    private UUID id;

    @DBRef(lazy = true)
    private Client client;

    private Date startTime;
    private Date endTime;

    private List<Treatment> treatments = new LinkedList<>();

    private List<Purchase> purchases = new LinkedList<>();

    Appointment() {
        //do nothing
    }

    public Appointment(UUID id, Client client, Date startTime, Date endTime, List<Treatment> treatments) {
        this(client, startTime, endTime, treatments);
        setId(id);
    }

    public Appointment(Client client, Date startTime, Date endTime, List<Treatment> treatments) {
        this();
        this.id = UUID.randomUUID();
        this.client = Objects.requireNonNull(client, "client can not be null");
        this.startTime = Objects.requireNonNull(startTime, "startTime can not be null");
        this.endTime = Objects.requireNonNull(endTime, "endTime can not be null");
        this.treatments.addAll(treatments);
    }

    @Override
    public UUID getId() {
        return id;
    }

    protected void setId(UUID id) {
        this.id = id == null ? UUID.randomUUID() : id;
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

    public void addAllPurchases(List<Purchase> purchases) {
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
