package com.example.salon.domain;

import org.bson.types.ObjectId;

import java.util.Objects;

public class Purchase extends AbstractDocument<String> {

    private String id;
    private String name;
    private double price;
    private long loyaltyPoints;


    Purchase() {
        //do nothing
    }

    public Purchase(String name, double price, long loyaltyPoints) {
        this();
        this.id = ObjectId.get().toString();
        this.name = Objects.requireNonNull(name, "name can not be null");
        this.price = price;
        this.loyaltyPoints = loyaltyPoints;
    }


    @Override
    public String getId() {
        return id;
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