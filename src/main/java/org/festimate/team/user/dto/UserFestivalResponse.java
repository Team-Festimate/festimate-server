package org.festimate.team.user.dto;

import org.festimate.team.festival.entity.Festival;

import java.time.format.DateTimeFormatter;

public record UserFestivalResponse(
        long festivalId,
        String title,
        String startDate,
        String endDate
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static UserFestivalResponse from(Festival festival) {
        return new UserFestivalResponse(
                festival.getFestivalId(),
                festival.getTitle(),
                festival.getStartDate().format(FORMATTER),
                festival.getEndDate().format(FORMATTER)
        );
    }
}
