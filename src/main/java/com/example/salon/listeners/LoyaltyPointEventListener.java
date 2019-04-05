package com.example.salon.listeners;

import com.example.salon.service.ClientService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LoyaltyPointEventListener {

    private final ClientService clientService;

    LoyaltyPointEventListener(ClientService clientService){
        this.clientService = Objects.requireNonNull(clientService, "clientService can not be null");
    }

    @EventListener
    //TODO change to transactional Event listener
    //TODO make this Async
    public void handle(LoyaltyPointEvent event) {
        this.clientService.incrementClientLoyaltyPoints(event);
    }

}
