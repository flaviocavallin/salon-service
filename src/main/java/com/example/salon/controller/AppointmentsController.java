package com.example.salon.controller;

import com.example.salon.dto.AppointmentDTO;
import com.example.salon.dto.PurchaseDTO;
import com.example.salon.service.AppointmentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1/appointments", consumes = MediaType.APPLICATION_JSON_VALUE)
public class AppointmentsController {

    private final AppointmentService appointmentService;

    AppointmentsController(AppointmentService appointmentService) {
        this.appointmentService = Objects.requireNonNull(appointmentService, "appointmentService can not be null");
    }

    @PostMapping
    //TODO Add validations
    public void create(@RequestBody AppointmentDTO appointmentDTO) {
        this.appointmentService.create(appointmentDTO);
    }

    @PostMapping("/{appointmentId}/purchase")
    public void purchase(@PathVariable("appointmentId") String appointmentId, @RequestBody PurchaseDTO purchaseDTO) {
        this.appointmentService.addPurchase(appointmentId, purchaseDTO);

    }
}
