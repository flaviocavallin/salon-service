package com.example.salon.service.parser;

import com.example.salon.domain.Client;
import com.example.salon.dto.AppointmentDTO;
import com.example.salon.repository.AppointmentRepository;
import com.example.salon.repository.ClientRepository;
import com.example.salon.util.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class AppointmentConsumerTest extends IntegrationTest {

    @Autowired
    private AppointmentConsumer appointmentConsumer;

    private AppointmentDTO appointmentDTO;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Before
    public void setUp() {

        Client client = new Client("firstName1", "lastName1", "email1", "phone1", "Male");
        client = clientRepository.save(client);

        this.appointmentDTO = new AppointmentDTO(client.getId(), new Date(), new Date());
    }

    @Test
    public void acceptTest() throws Exception {

        Assertions.assertThat(appointmentRepository.findAll().isEmpty()).isTrue();

        this.appointmentConsumer.accept(appointmentDTO);

        Assertions.assertThat(appointmentRepository.findAll().isEmpty()).isFalse();
        Assertions.assertThat(appointmentRepository.findAll().size()).isEqualTo(1);
    }

}
