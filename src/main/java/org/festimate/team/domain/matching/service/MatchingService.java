package org.festimate.team.domain.matching.service;

import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.api.matching.dto.MatchingInfo;
import org.festimate.team.domain.matching.entity.Matching;
import org.festimate.team.domain.participant.entity.Participant;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MatchingService {
    Matching createMatching(Festival festival, Optional<Participant> targetParticipantOptional, Participant participant);
    
    @Transactional
    Optional<Participant> findBestCandidateByPriority(long festivalId, Participant participant);

    void matchPendingParticipants(Participant newParticipant);

    List<MatchingInfo> getMatchingListByParticipant(Participant participant);
}
