package com.example.salon.repository;

import com.example.salon.domain.Client;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;

    @Before
    public void setUp(){
        this.firstName = "Dori";
        this.lastName = "Dietrich";
        this.email = "patrica@keeling.net";
        this.phone = "(272) 301-6356";
        this.gender = "Male";
    }

    @Test
    public void givenClient_saveIt_thenRetrievedClientFromDBTest(){
        Client client = new Client(firstName, lastName, email, phone, gender);

        clientRepository.save(client);

        List<Client> clients = clientRepository.findAll();
        Assertions.assertThat(clients).isNotEmpty();
        Assertions.assertThat(clients).size().isEqualTo(1);

        Client savedClient = clients.get(0);

        Assertions.assertThat(savedClient.getId()).isNotNull();
        Assertions.assertThat(savedClient.getFirstName()).isEqualTo(firstName);
        Assertions.assertThat(savedClient.getLastName()).isEqualTo(lastName);
        Assertions.assertThat(savedClient.getEmail()).isEqualTo(email);
        Assertions.assertThat(savedClient.getPhone()).isEqualTo(phone);
        Assertions.assertThat(savedClient.getGender()).isEqualTo(gender);
        Assertions.assertThat(savedClient.isBanned()).isFalse();
    }
}
