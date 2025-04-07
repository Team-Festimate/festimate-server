package org.festimate.team.festival.dto;

import org.festimate.team.participant.entity.TypeResult;

public record ProfileResponse(
        TypeResult typeResult,
        String nickname
) {
    public static ProfileResponse of(TypeResult typeResult, String nickname) {
        return new ProfileResponse(typeResult, nickname);
    }
}
