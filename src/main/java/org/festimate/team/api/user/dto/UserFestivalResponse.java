package org.festimate.team.api.user.dto;

import org.festimate.team.domain.festival.entity.Category;
import org.festimate.team.domain.festival.entity.Festival;

import java.time.format.DateTimeFormatter;

public record UserFestivalResponse(
        long festivalId,
        Category category,
        String title,
        String startDate,
        String endDate
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static UserFestivalResponse from(Festival festival) {
        return new UserFestivalResponse(
                festival.getFestivalId(),
                festival.getCategory(),
                festival.getTitle(),
                festival.getStartDate().format(FORMATTER),
                festival.getEndDate().format(FORMATTER)
        );
    }
}
