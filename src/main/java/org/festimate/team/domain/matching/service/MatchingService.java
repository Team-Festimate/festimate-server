package org.festimate.team.domain.matching.service;

import org.festimate.team.api.matching.dto.MatchingDetailInfo;
import org.festimate.team.api.matching.dto.MatchingListResponse;
import org.festimate.team.api.matching.dto.MatchingStatusResponse;
import org.festimate.team.domain.participant.entity.Participant;

import java.util.Optional;

public interface MatchingService {
    MatchingStatusResponse createMatching(Long userId, Long festivalId);

    MatchingListResponse getMatchingList(Long userId, Long festivalId);

    MatchingDetailInfo getMatchingDetail(Long userId, Long festivalId, Long matchingId);

    Optional<Participant> findBestCandidateByPriority(long festivalId, Participant participant);

    void matchPendingParticipants(Participant newParticipant);
}
