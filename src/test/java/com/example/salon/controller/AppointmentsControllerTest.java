package com.example.salon.controller;

import com.example.salon.SalonApplication;
import com.example.salon.dto.AppointmentDTO;
import com.example.salon.dto.PurchaseDTO;
import com.example.salon.dto.TreatmentDTO;
import com.example.salon.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.FastDateFormat;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentsControllerTest {

    private static final UUID CLIENT_ID = UUID.randomUUID();
    private static final Date START_TIME;
    private static final Date END_TIME;
    private static final String TREATMENT_NAME = "Full Head Colour";
    private static final double TREATMENT_PRICE = 85;
    private static final long TREATMENT_LOYALTY_POINTS = 80;
    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss Z");

    static {
        try {
            START_TIME = DATE_FORMAT.parse("2016-02-10 13:00:00 +0000");
        } catch (ParseException e) {
            throw new RuntimeException("Impossible ot parse startTime");
        }

        try {
            END_TIME = DATE_FORMAT.parse("2016-02-10 13:45:00 +0000");
        } catch (ParseException e) {
            throw new RuntimeException("Impossible ot parse endTime");
        }
    }

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void create_new_appointment_Test() throws Exception {
        String purchaseName = "Shampoo";
        double purchasePrice = 10;
        long purchaseLoyaltyPoints = 20;

        PurchaseDTO purchaseDTO = new PurchaseDTO(purchaseName, purchasePrice, purchaseLoyaltyPoints);

        TreatmentDTO treatmentDTO = new TreatmentDTO(TREATMENT_NAME, TREATMENT_PRICE, TREATMENT_LOYALTY_POINTS);

        AppointmentDTO dto = new AppointmentDTO(CLIENT_ID, START_TIME, END_TIME, Arrays.asList(treatmentDTO));
        dto.setPurchases(Arrays.asList(purchaseDTO));

        String content = objectMapper.writeValueAsString(dto);

        ArgumentCaptor<AppointmentDTO> captor = ArgumentCaptor.forClass(AppointmentDTO.class);

        Mockito.doNothing().when(appointmentService).save(captor.capture());

        mvc.perform(post("/api/v1/appointments").content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());

        AppointmentDTO appointmentDTO = captor.getValue();

        Assertions.assertThat(appointmentDTO.getClientId()).isEqualTo(CLIENT_ID);
        Assertions.assertThat(appointmentDTO.getStartTime()).isEqualTo(START_TIME);
        Assertions.assertThat(appointmentDTO.getEndTime()).isEqualTo(END_TIME);

        Assertions.assertThat(appointmentDTO.getTreatments().size()).isEqualTo(1);


        TreatmentDTO t = appointmentDTO.getTreatments().get(0);

        Assertions.assertThat(t.getName()).isEqualTo(TREATMENT_NAME);
        Assertions.assertThat(t.getPrice()).isEqualTo(TREATMENT_PRICE);
        Assertions.assertThat(t.getLoyaltyPoints()).isEqualTo(TREATMENT_LOYALTY_POINTS);


        Assertions.assertThat(appointmentDTO.getPurchases().size()).isEqualTo(1);

        PurchaseDTO p = appointmentDTO.getPurchases().get(0);

        Assertions.assertThat(p.getName()).isEqualTo(purchaseName);
        Assertions.assertThat(p.getPrice()).isEqualTo(purchasePrice);
        Assertions.assertThat(p.getLoyaltyPoints()).isEqualTo(purchaseLoyaltyPoints);

        Mockito.verify(appointmentService).save(appointmentDTO);
    }


    @Test
    public void given_appointmentId_then_delete() throws Exception {
        UUID appointmentId = UUID.randomUUID();

        ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);

        Mockito.doNothing().when(appointmentService).deleteById(captor.capture());

        mvc.perform(delete("/api/v1/appointments/{appointmentId}", appointmentId)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertThat(captor.getValue()).isEqualTo(appointmentId);

        Mockito.verify(appointmentService).deleteById(appointmentId);
    }


    @Test
    public void given_AppointmentID_and_PurchaseDTO_then_Register_Purchase() throws Exception {
        UUID appointmentId = UUID.randomUUID();
        String purchaseName = "Shampoo";
        double purchasePrice = 10;
        long purchaseLoyaltyPoints = 20;

        PurchaseDTO dto = new PurchaseDTO(purchaseName, purchasePrice, purchaseLoyaltyPoints);
        dto.setAppointmentId(appointmentId);

        String content = objectMapper.writeValueAsString(dto);

        ArgumentCaptor<PurchaseDTO> purchaseCaptor = ArgumentCaptor.forClass(PurchaseDTO.class);

        Mockito.doNothing().when(appointmentService).addPurchase(purchaseCaptor.capture());

        mvc.perform(post("/api/v1/appointments/purchase", appointmentId).content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());

        PurchaseDTO purchaseDTO = purchaseCaptor.getValue();

        Assertions.assertThat(purchaseDTO.getAppointmentId()).isEqualTo(appointmentId);
        Assertions.assertThat(purchaseDTO.getName()).isEqualTo(purchaseName);
        Assertions.assertThat(purchaseDTO.getPrice()).isEqualTo(purchasePrice);
        Assertions.assertThat(purchaseDTO.getLoyaltyPoints()).isEqualTo(purchaseLoyaltyPoints);


        Mockito.verify(appointmentService).addPurchase(purchaseDTO);
    }

}