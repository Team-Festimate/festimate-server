package org.festimate.team.festival.dto;

import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.entity.FestivalStatus;

public record AdminFestivalResponse(
        long festivalId,
        FestivalStatus status,
        String title,
        String inviteCode
) {
    public static AdminFestivalResponse of(Festival festival) {
        return new AdminFestivalResponse(
                festival.getFestivalId(),
                festival.getFestivalStatus(),
                festival.getTitle(),
                festival.getInviteCode()
        );
    }
}
