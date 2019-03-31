package com.example.salon.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

//@Document(collection = "purchases")
//@TypeAlias("purchase")
public class Purchase { // extends AbstractDocument<String> {
//
//    @Id
//    private String id;

//    private Appointment appointment;
    private String name;
    private double price;
    private long loyaltyPoints;


    Purchase() {
        //do nothing
    }

//    public Purchase(Appointment appointment, String name, double price, long loyaltyPoints) {
public Purchase(String name, double price, long loyaltyPoints) {
        this();
//        this.appointment = Objects.requireNonNull(appointment, "appointment can not be null");
        this.name = Objects.requireNonNull(name, "name can not be null");
        this.price = price;
        this.loyaltyPoints = loyaltyPoints;
    }


//    @Override
//    public String getId() {
//        return id;
//    }
//
//    public Appointment getAppointment() {
//        return appointment;
//    }

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
