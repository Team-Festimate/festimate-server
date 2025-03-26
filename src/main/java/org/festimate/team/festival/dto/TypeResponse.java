package org.festimate.team.festival.dto;

import org.festimate.team.participant.entity.TypeResult;

public record TypeResponse(TypeResult typeResult) {
    public static TypeResponse from(TypeResult typeResult) {
        return new TypeResponse(typeResult);
    }
}
