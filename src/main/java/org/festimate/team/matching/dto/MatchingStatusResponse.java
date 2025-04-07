package org.festimate.team.matching.dto;

import org.festimate.team.matching.entity.MatchingStatus;

public record MatchingStatusResponse(
        MatchingStatus status,
        Long matchingId
) {
    public static MatchingStatusResponse of(MatchingStatus status, Long matchingId) {
        return new MatchingStatusResponse(status, matchingId);
    }
}
