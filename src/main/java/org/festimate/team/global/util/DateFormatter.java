package org.festimate.team.global.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static String formatPeriod(LocalDate startDate, LocalDate endDate) {
        return startDate.format(FORMATTER) + " ~ " + endDate.format(FORMATTER);
    }

    public static String formatSingle(LocalDate date) {
        return date.format(FORMATTER);
    }
}
