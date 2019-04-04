package com.example.salon.service.parser;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.Client;
import com.example.salon.repository.AppointmentRepository;
import com.example.salon.repository.ClientRepository;
import com.example.salon.util.IntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.UUID;

public class AppointmentCsvFileProcessorTest extends IntegrationTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Autowired
    private AppointmentCsvFileProcessor fileProcessor;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ClientRepository clientRepository;
    private File file;
    private UUID clientId = UUID.randomUUID();
    private UUID appointmentId = UUID.randomUUID();

    @Before
    public void setUp() throws IOException {
        Path storeLocation = Paths.get(System.getProperty("java.io.tmpdir"));


        Random random = new Random();
        String text = "id,client_id,start_time,end_time\n" +
                appointmentId.toString() + "," + clientId.toString() + ",2016-02-07 00:00:00 +0000," +
                "2016-02-07 00:00:00 +0000";
        MockMultipartFile multipartFile = new MockMultipartFile("file" + random.nextLong(),
                "appointments" + random.nextLong() + ".csv", "text/plain", text.getBytes());

        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, storeLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        }


        Path p = storeLocation.resolve(filename);
        file = new File(p.toAbsolutePath().toString());


        Client client = new Client(clientId, "firstName1", "lastName1", "email1", "phone1", "Male",
                false);
        clientRepository.save(client);
    }

    @Test
    public void processTest() {
        Assertions.assertThat(appointmentRepository.findAll()).isEmpty();

        fileProcessor.process(file);

        Assertions.assertThat(appointmentRepository.findAll()).isNotEmpty();

        Appointment appointment = appointmentRepository.findAll().get(0);
        Assertions.assertThat(appointment.getId()).isEqualTo(appointmentId);
        Assertions.assertThat(appointment.getClient().getId()).isEqualTo(clientId);
    }
}