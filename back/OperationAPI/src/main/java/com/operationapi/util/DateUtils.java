package com.operationapi.util;

import com.operationapi.exception.FunctionalException;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

public class DateUtils {

    public static OffsetDateTime parseDate(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        try {
            return OffsetDateTime.parse(date);
        } catch (DateTimeParseException e) {
            throw new FunctionalException("400", "Le format de date est invalide : " + date);
        }
    }
}
