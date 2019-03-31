package com.example.salon.factory;

import com.example.salon.domain.Client;
import com.example.salon.dto.ClientDTO;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class ClientDTOFactoryTest {

    private ClientDTOFactory factory;

    @Before
    public void setUp() {
        factory = new ClientDTOFactory();
    }

    @Test
    public void given_ClientDTO_then_getClient() {
        String firstName = "name1";
        String lastName = "lastName1";
        String email = "a1@a1.com";
        String phone = "123";
        String gender = "Male";

        ClientDTO clientDTO = new ClientDTO(firstName, lastName, email, phone, gender);

        Client client = factory.fromDTO(clientDTO);

        Assertions.assertThat(client.getFirstName()).isEqualTo(firstName);
        Assertions.assertThat(client.getLastName()).isEqualTo(lastName);
        Assertions.assertThat(client.getEmail()).isEqualTo(email);
        Assertions.assertThat(client.getPhone()).isEqualTo(phone);
        Assertions.assertThat(client.getGender()).isEqualTo(gender);
    }


    @Test
    public void given_Client_then_getClientDTO() {
        String firstName = "name1";
        String lastName = "lastName1";
        String email = "a1@a1.com";
        String phone = "123";
        String gender = "Male";

        Client client = new Client(firstName, lastName, email, phone, gender);

        ClientDTO clientDTO = factory.toDTO(client);

        Assertions.assertThat(clientDTO.getFirstName()).isEqualTo(firstName);
        Assertions.assertThat(clientDTO.getLastName()).isEqualTo(lastName);
        Assertions.assertThat(clientDTO.getEmail()).isEqualTo(email);
        Assertions.assertThat(clientDTO.getPhone()).isEqualTo(phone);
        Assertions.assertThat(clientDTO.getGender()).isEqualTo(gender);
    }
}
