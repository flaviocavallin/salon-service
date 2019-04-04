package com.example.salon.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clients")
@TypeAlias("client")
public class Client extends AbstractDocument<UUID> {

	private static final long serialVersionUID = -9059148646435022250L;

	@Id
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;
    private boolean banned;

    private List<LoyaltyPoint> loyaltyPoints = new LinkedList<>();

    Client() {
        //do nothing
    }

    public Client(String firstName, String lastName, String email, String phone, String gender) {
        this();
        this.id = UUID.randomUUID();
        this.firstName = Objects.requireNonNull(firstName, "firstName can not be null");
        this.lastName = Objects.requireNonNull(lastName, "lastName can not be null");
        this.email = Objects.requireNonNull(email, "email can not be null");
        this.phone = Objects.requireNonNull(phone, "phone can not be null");
        this.gender = Objects.requireNonNull(gender, "gender can not be null");
    }

    public Client(UUID id, String firstName, String lastName, String email, String phone, String gender,
                  Boolean banned) {
        this(firstName, lastName, email, phone, gender);
        setId(id);
        setBanned(banned);
    }


    @Override
    public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id == null ? UUID.randomUUID() : id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {

        this.banned = banned == null ? false : banned;
    }

    public void addLoyaltyPoints(LoyaltyPoint loyaltyPoint) {
        Objects.requireNonNull(loyaltyPoint, "loyaltyPoint can not be null");

        this.loyaltyPoints.add(loyaltyPoint);
    }

    public List<LoyaltyPoint> getLoyaltyPoints() {
        return loyaltyPoints;
    }
}
