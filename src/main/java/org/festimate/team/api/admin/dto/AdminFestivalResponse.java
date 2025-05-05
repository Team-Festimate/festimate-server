package org.festimate.team.api.admin.dto;

import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.entity.FestivalStatus;

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
