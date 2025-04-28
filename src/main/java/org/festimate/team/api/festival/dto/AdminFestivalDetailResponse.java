package org.festimate.team.api.festival.dto;

import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.entity.FestivalStatus;

import static org.festimate.team.global.util.DateFormatter.formatPeriod;

public record AdminFestivalDetailResponse(
        FestivalStatus status,
        String title,
        String festivalDate,
        String inviteCode
) {
    public static AdminFestivalDetailResponse of(Festival festival) {
        return new AdminFestivalDetailResponse(
                festival.getFestivalStatus(),
                festival.getTitle(),
                formatPeriod(festival.getStartDate(), festival.getEndDate()),
                festival.getInviteCode()
        );
    }
}
