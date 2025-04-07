package org.festimate.team.festival.dto;

import org.festimate.team.participant.entity.Participant;
import org.festimate.team.participant.entity.TypeResult;

public record MainUserInfoResponse(
        TypeResult typeResult,
        int point
) {
    public static MainUserInfoResponse from(Participant participant, int point) {
        return new MainUserInfoResponse(
                participant.getTypeResult(),
                point
        );
    }
}
