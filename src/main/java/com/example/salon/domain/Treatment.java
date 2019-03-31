package com.example.salon.domain;

import java.util.Objects;

public class Treatment {

    private String name;
    private double price;
    private long loyaltyPoints;

    Treatment() {
        //do nothing
    }

    public Treatment(String name, double price, long loyaltyPoints) {
        this();
        this.name = Objects.requireNonNull(name, "name can not be null");
        this.price = price;
        this.loyaltyPoints = loyaltyPoints;
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
