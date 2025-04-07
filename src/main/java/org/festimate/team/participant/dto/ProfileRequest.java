package org.festimate.team.participant.dto;

import org.festimate.team.participant.entity.TypeResult;

public record ProfileRequest(
        TypeResult typeResult,
        String introduction,
        String message
) {
}
