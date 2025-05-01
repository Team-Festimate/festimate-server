package org.festimate.team.api.participant.dto;

import org.festimate.team.domain.festival.entity.FestivalStatus;
import org.festimate.team.domain.participant.entity.TypeResult;

public record ProfileResponse(
        TypeResult typeResult,
        String nickname,
        FestivalStatus status
) {
    public static ProfileResponse of(TypeResult typeResult, String nickname, FestivalStatus status) {
        return new ProfileResponse(typeResult, nickname, status);
    }
}
