package com.example.salon.controller;

import com.example.salon.dto.ClientDTO;
import com.example.salon.service.ClientService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1/clients", consumes = MediaType.APPLICATION_JSON_VALUE)
public class ClientsController {

    private final ClientService clientService;

    ClientsController(ClientService clientService){
        this.clientService = Objects.requireNonNull(clientService, "clientService can not be null");
    }

    @PostMapping
    //TODO Add validations
    public void create(@RequestBody ClientDTO clientDTO){
        this.clientService.create(clientDTO);
    }

    @GetMapping(value="/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    //TODO Add error exception handling
    public ClientDTO get(@PathVariable("clientId") String clientId){
        return this.clientService.getById(clientId);
    }
}
