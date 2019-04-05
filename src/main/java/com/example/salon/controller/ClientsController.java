package com.example.salon.controller;

import com.example.salon.dto.ClientDTO;
import com.example.salon.dto.PointedClientDTO;
import com.example.salon.service.ClientService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/clients", consumes = MediaType.APPLICATION_JSON_VALUE)
public class ClientsController {

    private final ClientService clientService;

    ClientsController(ClientService clientService) {
        this.clientService = Objects.requireNonNull(clientService, "clientService can not be null");
    }

    @PostMapping
    //TODO Add validations
    //TODO return client
    public void create(@RequestBody ClientDTO clientDTO) {
        this.clientService.save(clientDTO);
    }

    @GetMapping(value = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientDTO get(@PathVariable("clientId") UUID clientId) {
        return this.clientService.getById(clientId);
    }

    @DeleteMapping(value = "/{clientId}")
    public void delete(@PathVariable("clientId") UUID clientId) {
        this.clientService.deleteById(clientId);
    }


    //TODO add page limit
    @GetMapping("/top/{limit}/loyal")
    public List<PointedClientDTO> getTopClients(@PathVariable("limit") int limit,
                                                @RequestParam(value = "dateFrom") @DateTimeFormat(pattern = "yyyy-MM" +
                                                        "-dd") LocalDate dateFrom) {

        return this.clientService.getTopMostLoyalActiveClientsBy(dateFrom, LocalDate.now(), limit);
    }

}