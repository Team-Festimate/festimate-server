package org.festimate.team.matching.dto;

import org.festimate.team.matching.entity.MatchingStatus;

import java.time.LocalDateTime;

public record MatchingStatusResponse(
        MatchingStatus status,
        Long matchingId,
        LocalDateTime matchDate
) {
    public static MatchingStatusResponse of(MatchingStatus status, Long matchingId, LocalDateTime matchDate) {
        return new MatchingStatusResponse(status, matchingId, matchDate);
    }
}
