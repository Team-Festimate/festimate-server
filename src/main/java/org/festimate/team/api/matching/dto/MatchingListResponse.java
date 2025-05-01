package org.festimate.team.api.matching.dto;

import java.util.List;

public record MatchingListResponse(
        int matchingCount,
        List<MatchingInfo> matchingList
) {
    public static MatchingListResponse from(List<MatchingInfo> matchingList) {
        return new MatchingListResponse(matchingList.size(), matchingList);
    }
}
