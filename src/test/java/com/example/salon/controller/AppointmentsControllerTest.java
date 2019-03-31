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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = SalonApplication.class)
@AutoConfigureMockMvc
public class AppointmentsControllerTest {

    private static final String CLIENT_ID = "5c9fd8360884791e7e28978b";
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
        TreatmentDTO treatmentDTO = new TreatmentDTO(TREATMENT_NAME, TREATMENT_PRICE, TREATMENT_LOYALTY_POINTS);

        AppointmentDTO dto = new AppointmentDTO(CLIENT_ID, START_TIME, END_TIME, Arrays.asList(treatmentDTO));

        String content = objectMapper.writeValueAsString(dto);

        ArgumentCaptor<AppointmentDTO> captor = ArgumentCaptor.forClass(AppointmentDTO.class);

        Mockito.doNothing().when(appointmentService).create(captor.capture());

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

        Mockito.verify(appointmentService).create(appointmentDTO);
    }


    @Test
    public void given_AppointmentID_and_PurchaseDTO_then_Register_Purchase() throws Exception {
        String appointmentId = "123";

        String purchaseName = "Shampoo";
        double purchasePrice = 10;
        long purchaseLoyaltyPoints = 20;

        PurchaseDTO dto = new PurchaseDTO(purchaseName, purchasePrice, purchaseLoyaltyPoints);

        String content = objectMapper.writeValueAsString(dto);

        ArgumentCaptor<PurchaseDTO> purchaseCaptor = ArgumentCaptor.forClass(PurchaseDTO.class);
        ArgumentCaptor<String> appointmentIdCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.doNothing().when(appointmentService).addPurchase(appointmentIdCaptor.capture(),
                purchaseCaptor.capture());

        mvc.perform(post("/api/v1/appointments/{appointmentId}/purchase", appointmentId).content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());

        String appId = appointmentIdCaptor.getValue();

        Assertions.assertThat(appId).isEqualTo(appointmentId);

        PurchaseDTO purchaseDTO = purchaseCaptor.getValue();

        Assertions.assertThat(purchaseDTO.getName()).isEqualTo(purchaseName);
        Assertions.assertThat(purchaseDTO.getPrice()).isEqualTo(purchasePrice);
        Assertions.assertThat(purchaseDTO.getLoyaltyPoints()).isEqualTo(purchaseLoyaltyPoints);


        Mockito.verify(appointmentService).addPurchase(appId, purchaseDTO);
    }
}