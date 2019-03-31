package com.example.salon.dto;

import java.util.Objects;

public class PurchaseDTO {

    private String name;
    private double price;
    private long loyaltyPoints;

    PurchaseDTO() {
        //do nothing
    }

    public PurchaseDTO(String name, double price, long loyaltyPoints) {
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