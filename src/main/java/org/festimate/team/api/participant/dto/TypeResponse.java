package org.festimate.team.api.participant.dto;

import org.festimate.team.domain.participant.entity.TypeResult;

public record TypeResponse(TypeResult typeResult) {
    public static TypeResponse from(TypeResult typeResult) {
        return new TypeResponse(typeResult);
    }
}
