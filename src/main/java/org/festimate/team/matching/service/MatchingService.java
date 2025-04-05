package org.festimate.team.matching.service;

import org.festimate.team.festival.entity.Festival;
import org.festimate.team.matching.dto.MatchingInfo;
import org.festimate.team.matching.entity.Matching;
import org.festimate.team.participant.entity.Participant;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MatchingService {
    Matching createMatching(Festival festival, Optional<Participant> targetParticipantOptional, Participant participant);
    
    @Transactional
    Optional<Participant> findBestCandidateByPriority(long festivalId, Participant participant);

    void matchPendingParticipants(Participant newParticipant);

    List<MatchingInfo> getMatchingListByParticipant(Participant participant);

    Matching getMatchingDetailById(Participant participant, long matchingId);
}
