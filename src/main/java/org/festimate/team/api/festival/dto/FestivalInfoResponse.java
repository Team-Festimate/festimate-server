package org.festimate.team.api.festival.dto;

import org.festimate.team.domain.festival.entity.Festival;

import static org.festimate.team.global.util.DateFormatter.formatPeriod;

public record FestivalInfoResponse(
        String festivalName,
        String festivalDate
) {
    public static FestivalInfoResponse of(Festival festival) {
        return new FestivalInfoResponse(
                festival.getTitle(),
                formatPeriod(festival.getStartDate(), festival.getEndDate())
        );
    }
}
