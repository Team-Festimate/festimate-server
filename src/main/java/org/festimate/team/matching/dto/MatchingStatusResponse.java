package org.festimate.team.matching.dto;

import org.festimate.team.matching.entity.MatchingStatus;

public record MatchingStatusResponse(
        MatchingStatus status,
        long matchingId
) {
    public static MatchingStatusResponse of(MatchingStatus status, long matchingId) {
        return new MatchingStatusResponse(status, matchingId);
    }
}
