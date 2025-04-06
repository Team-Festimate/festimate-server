package org.festimate.team.festival.dto;

import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.entity.FestivalStatus;

import static org.festimate.team.common.util.DateFormatter.formatPeriod;

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
