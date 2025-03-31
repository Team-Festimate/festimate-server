package org.festimate.team.festival.dto;

import org.festimate.team.festival.entity.Festival;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record FestivalInfoResponse(
        String festivalName,
        String festivalDate
) {
    public static FestivalInfoResponse of(Festival festival) {
        return new FestivalInfoResponse(
                festival.getTitle(),
                formattingDate(festival.getStartDate(), festival.getEndDate())
        );
    }

    private static String formattingDate(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return startDate.format(formatter) + " ~ " + endDate.format(formatter);
    }
}
