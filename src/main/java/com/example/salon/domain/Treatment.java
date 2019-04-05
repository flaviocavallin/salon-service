package com.example.salon.domain;

import java.util.Objects;
import java.util.UUID;

public class Treatment extends AbstractDocument<UUID> {

	private static final long serialVersionUID = -8470976381761924281L;
	private UUID id;
    private String name;
    private double price;
    private long loyaltyPoints;

    Treatment() {
        //do nothing
    }

    public Treatment(UUID id, String name, double price, long loyaltyPoints) {
        this(name, price, loyaltyPoints);
        this.id = id;
    }

    public Treatment(String name, double price, long loyaltyPoints) {
        this();
        this.id = UUID.randomUUID();
        this.name = Objects.requireNonNull(name, "name can not be null");
        this.price = price;
        this.loyaltyPoints = loyaltyPoints;
    }


    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public long getLoyaltyPoints() {
        return loyaltyPoints;
    }

}
