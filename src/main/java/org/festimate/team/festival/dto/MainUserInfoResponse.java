package org.festimate.team.festival.dto;

import org.festimate.team.festival.entity.FestivalStatus;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.participant.entity.TypeResult;

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
