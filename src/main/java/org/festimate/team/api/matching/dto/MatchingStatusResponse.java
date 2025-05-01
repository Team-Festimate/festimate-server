package org.festimate.team.api.matching.dto;

import org.festimate.team.domain.matching.entity.MatchingStatus;

public record MatchingStatusResponse(
        MatchingStatus status,
        Long matchingId
) {
    public static MatchingStatusResponse of(MatchingStatus status, Long matchingId) {
        return new MatchingStatusResponse(status, matchingId);
    }
}
