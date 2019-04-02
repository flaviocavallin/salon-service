package com.example.salon.factory;

import com.example.salon.domain.Appointment;
import com.example.salon.domain.LoyaltyPoint;
import com.example.salon.listeners.LoyaltyPointEvent;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Component
public class LoyaltyPointEventFactory {

    LoyaltyPointEventFactory(){
        //do nothing
    }

    public LoyaltyPointEvent convert(Appointment appointment){
        Objects.requireNonNull(appointment, "appointment can not be null");

        String clientId = appointment.getClient().getId();
        LocalDate date = convertToLocalDateViaInstant(appointment.getStartTime());

        long points = 0;

        if (CollectionUtils.isNotEmpty(appointment.getTreatments())) {
            points = appointment.getTreatments().stream().mapToLong(t -> t.getLoyaltyPoints()).sum();
        }

        if (CollectionUtils.isNotEmpty(appointment.getPurchases())) {
            points = points + appointment.getPurchases().stream().mapToLong(p -> p.getLoyaltyPoints()).sum();
        }

        return new LoyaltyPointEvent(clientId, date, points);
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
