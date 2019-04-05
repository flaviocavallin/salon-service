package com.example.salon.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    private DateUtil() {
        //do nothing
    }


    public static LocalDate convertToLocaDate(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }

        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
