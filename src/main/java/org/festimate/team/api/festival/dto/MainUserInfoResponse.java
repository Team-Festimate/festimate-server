package org.festimate.team.api.festival.dto;

import org.festimate.team.domain.festival.entity.FestivalStatus;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.TypeResult;

public record MainUserInfoResponse(
        TypeResult typeResult,
        int point,
        FestivalStatus status
) {
    public static MainUserInfoResponse from(Participant participant, int point, FestivalStatus status) {
        return new MainUserInfoResponse(
                participant.getTypeResult(),
                point,
                status
        );
    }
}
