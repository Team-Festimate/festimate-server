package org.festimate.team.api.participant.dto;

import org.festimate.team.domain.participant.entity.TypeResult;

public record ProfileRequest(
        TypeResult typeResult,
        String introduction,
        String message
) {
}
