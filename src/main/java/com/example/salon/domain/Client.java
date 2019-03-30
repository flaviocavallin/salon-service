package com.example.salon.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "clients")
@TypeAlias("client")
public class Client extends AbstractDocument<String> {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;
    private boolean banned;

    Client() {
        //do nothing
    }

    public Client(String firstName, String lastName, String email, String phone, String gender) {
        this();
        this.firstName = Objects.requireNonNull(firstName, "firstName can not be null");
        this.lastName = Objects.requireNonNull(lastName, "lastName can not be null");
        this.email = Objects.requireNonNull(email, "email can not be null");
        this.phone = Objects.requireNonNull(phone, "phone can not be null");
        this.gender = Objects.requireNonNull(gender, "gender can not be null");
    }

    public Client(String id, String firstName, String lastName, String email, String phone, String gender,
                  boolean banned) {
        this(firstName, lastName, email, phone, gender);
        this.id = Objects.requireNonNull(id, "id can not be null");
        this.banned = banned;
    }


    @Override
    public String getId() {
        return id;
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
}
