package com.example.salon.repository.impl;

import com.example.salon.domain.Client;
import com.example.salon.domain.LoyaltyPoint;
import com.example.salon.domain.PointedClient;
import com.example.salon.repository.ClientRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ClientRepositoryImplTest {

    @Autowired
    private ClientRepository clientRepository;

    private Client client1, client2, client3, client4;

    @Before
    public void setUp() {
        Client client = new Client("firstName1", "lastName1", "email1", "phone1", "Male");
        client.addLoyaltyPoints(new LoyaltyPoint(LocalDate.of(2019, 1, 2), 3));
        client.addLoyaltyPoints(new LoyaltyPoint(LocalDate.of(2019, 1, 3), 1));
        client.addLoyaltyPoints(new LoyaltyPoint(LocalDate.of(2019, 1, 4), 5));

        client1 = clientRepository.save(client);


        client = new Client("firstName2", "lastName2", "email2", "phone2", "Male");
        client.addLoyaltyPoints(new LoyaltyPoint(LocalDate.of(2019, 1, 1), 40));
        client.addLoyaltyPoints(new LoyaltyPoint(LocalDate.of(2019, 1, 3), 30));
        client.addLoyaltyPoints(new LoyaltyPoint(LocalDate.of(2019, 1, 4), 50));
        client2 = clientRepository.save(client);


        client = new Client("firstName3", "lastName3", "email3", "phone3", "Male");
        client.addLoyaltyPoints(new LoyaltyPoint(LocalDate.of(2019, 1, 1), 40));
        client.addLoyaltyPoints(new LoyaltyPoint(LocalDate.of(2019, 1, 3), 30));
        client.addLoyaltyPoints(new LoyaltyPoint(LocalDate.of(2019, 1, 4), 50));
        client.setBanned(true);

        client3 = clientRepository.save(client);

        client = new Client("firstName4", "lastName4", "email4", "phone4", "Male");
        client.setBanned(true);

        client4 = clientRepository.save(client);
    }


    @Test
    public void given_clientsInsideDatabase_then_check_amount_Is_4() {
        Assertions.assertThat(clientRepository.findAll()).size().isEqualTo(4);
    }

    @Test
    public void given_clientsInsideDB_then_get_MostLoyalClients_Between_2019_01_03_and_2019_01_03() {
        List<PointedClient> pointedClientList = clientRepository.getTopMostLoyalActiveClientsBy(LocalDate.of(2019, 1, 3)
                , LocalDate.of(2019, 1, 3), 100);

        Assertions.assertThat(pointedClientList).size().isEqualTo(2);
        Assertions.assertThat(pointedClientList.get(0).getId()).isEqualTo(client2.getId());
        Assertions.assertThat(pointedClientList.get(1).getId()).isEqualTo(client1.getId());

        Assertions.assertThat(pointedClientList.get(0).getEmail()).isEqualTo(client2.getEmail());
        Assertions.assertThat(pointedClientList.get(1).getEmail()).isEqualTo(client1.getEmail());

        Assertions.assertThat(pointedClientList.get(0).getPoints()).isEqualTo(30);
        Assertions.assertThat(pointedClientList.get(1).getPoints()).isEqualTo(1);
    }

    @Test
    public void given_clientsInsideDB_then_get_MostLoyalClients_Between_2019_01_03_and_2019_01_04() {
        List<PointedClient> pointedClientList = clientRepository.getTopMostLoyalActiveClientsBy(LocalDate.of(2019, 1,
                3), LocalDate.of(2019,
                1, 4), 100);

        Assertions.assertThat(pointedClientList).size().isEqualTo(2);
        Assertions.assertThat(pointedClientList.get(0).getId()).isEqualTo(client2.getId());
        Assertions.assertThat(pointedClientList.get(1).getId()).isEqualTo(client1.getId());

        Assertions.assertThat(pointedClientList.get(0).getEmail()).isEqualTo(client2.getEmail());
        Assertions.assertThat(pointedClientList.get(1).getEmail()).isEqualTo(client1.getEmail());

        Assertions.assertThat(pointedClientList.get(0).getPoints()).isEqualTo(80);
        Assertions.assertThat(pointedClientList.get(1).getPoints()).isEqualTo(6);
    }

    @Test
    public void given_clientsInsideDB_then_get_TheOneMostLoyalClient_Between_2019_01_03_and_2019_01_04() {
        List<PointedClient> pointedClientList = clientRepository.getTopMostLoyalActiveClientsBy(LocalDate.of(2019, 1,
                3), LocalDate.of(2019,
                1, 4), 1);
        Assertions.assertThat(pointedClientList.size()).isEqualTo(1);
        Assertions.assertThat(pointedClientList.get(0).getId()).isEqualTo(client2.getId());
        Assertions.assertThat(pointedClientList.get(0).getEmail()).isEqualTo(client2.getEmail());
        Assertions.assertThat(pointedClientList.get(0).getPoints()).isEqualTo(80);
    }


    @Test
    public void given_clientId_And_client_has_loyaltyPoints_Then_increment_clientLoyaltyPoints() {
        String clientId = client1.getId();

        Client c = clientRepository.findById(clientId).get();

        LocalDate date = LocalDate.of(2019, 1, 2);
        long points = 3;

        LoyaltyPoint loyaltyPoint = c.getLoyaltyPoints().get(0);

        Assertions.assertThat(loyaltyPoint.getDate()).isEqualTo(date);
        Assertions.assertThat(loyaltyPoint.getPoints()).isEqualTo(points);


        long pointsToIncrement = 6;
        clientRepository.incrementLoyaltyPoints(clientId, date, pointsToIncrement);


        c = clientRepository.findById(clientId).get();
        loyaltyPoint = c.getLoyaltyPoints().get(0);

        Assertions.assertThat(loyaltyPoint.getDate()).isEqualTo(date);
        Assertions.assertThat(loyaltyPoint.getPoints()).isEqualTo(points + pointsToIncrement);
    }

    @Test
    public void given_client_Without_LoyaltyPoints_Then_assign_loyaltyPointToTheGivenDate() {
        String clientId = client4.getId();

        Client c = clientRepository.findById(clientId).get();

        Assertions.assertThat(c.getLoyaltyPoints()).isEmpty();

        LocalDate date = LocalDate.of(2019, 1, 5);
        long points = 7;
        clientRepository.incrementLoyaltyPoints(clientId, date, points);


        c = clientRepository.findById(clientId).get();

        Assertions.assertThat(c.getLoyaltyPoints()).isNotEmpty();

        Assertions.assertThat(c.getLoyaltyPoints().size()).isEqualTo(1);
        LoyaltyPoint loyaltyPoint = c.getLoyaltyPoints().get(0);

        Assertions.assertThat(loyaltyPoint.getDate()).isEqualTo(date);
        Assertions.assertThat(loyaltyPoint.getPoints()).isEqualTo(points);
    }
}