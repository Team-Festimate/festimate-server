package org.festimate.team.matching.service;

import org.festimate.team.festival.entity.Festival;
import org.festimate.team.matching.entity.Matching;
import org.festimate.team.participant.entity.Participant;

import java.util.Optional;

public interface MatchingService {
    Matching createMatching(Festival festival, Optional<Participant> targetParticipantOptional, Participant participant);

    Optional<Participant> findMatchingCandidate(long festivalId, Participant participant);

    void matchPendingParticipants(Participant newParticipant);
}
