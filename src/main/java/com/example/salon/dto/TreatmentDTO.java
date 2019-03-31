package com.example.salon.dto;

import java.util.Objects;

public class TreatmentDTO {

    private String name;
    private double price;
    private long loyaltyPoints;

    TreatmentDTO() {
        //do nothing
    }

    public TreatmentDTO(String name, double price, long loyaltyPoints) {
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
