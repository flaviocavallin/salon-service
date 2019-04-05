package com.example.salon.factory;

import com.example.salon.domain.Appointment;
import com.example.salon.listeners.LoyaltyPointEvent;
import com.example.salon.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component
public class LoyaltyPointEventFactory {

    LoyaltyPointEventFactory() {
        //do nothing
    }

    public LoyaltyPointEvent convert(Appointment appointment) {
        Objects.requireNonNull(appointment, "appointment can not be null");

        UUID clientId = appointment.getClient().getId();
        LocalDate date = DateUtil.convertToLocaDate(appointment.getStartTime());

        long points = 0;

        if (CollectionUtils.isNotEmpty(appointment.getTreatments())) {
            points = appointment.getTreatments().stream().mapToLong(t -> t.getLoyaltyPoints()).sum();
        }

        if (CollectionUtils.isNotEmpty(appointment.getPurchases())) {
            points = points + appointment.getPurchases().stream().mapToLong(p -> p.getLoyaltyPoints()).sum();
        }

        return new LoyaltyPointEvent(this, clientId, date, points);
    }
}
