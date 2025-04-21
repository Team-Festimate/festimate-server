package org.festimate.team.festival.dto;

import org.festimate.team.festival.entity.FestivalStatus;
import org.festimate.team.participant.entity.TypeResult;

public record ProfileResponse(
        TypeResult typeResult,
        String nickname,
        FestivalStatus status
) {
    public static ProfileResponse of(TypeResult typeResult, String nickname, FestivalStatus status) {
        return new ProfileResponse(typeResult, nickname, status);
    }
}
