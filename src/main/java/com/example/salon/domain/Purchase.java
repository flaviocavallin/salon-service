package com.example.salon.domain;

import java.util.Objects;
import java.util.UUID;

public class Purchase extends AbstractDocument<UUID> {

	private static final long serialVersionUID = -7753825769916814247L;
	private UUID id;
    private String name;
    private double price;
    private long loyaltyPoints;


    Purchase() {
        //do nothing
    }

    public Purchase(UUID id, String name, double price, long loyaltyPoints) {
        this(name, price, loyaltyPoints);
        this.id = id;
    }

    public Purchase(String name, double price, long loyaltyPoints) {
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