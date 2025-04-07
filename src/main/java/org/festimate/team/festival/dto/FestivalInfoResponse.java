package org.festimate.team.festival.dto;

import org.festimate.team.festival.entity.Festival;

import static org.festimate.team.common.util.DateFormatter.formatPeriod;

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
