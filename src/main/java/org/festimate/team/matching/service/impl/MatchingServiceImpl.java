package org.festimate.team.matching.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.matching.entity.Matching;
import org.festimate.team.matching.entity.MatchingStatus;
import org.festimate.team.matching.repository.MatchingRepository;
import org.festimate.team.matching.service.MatchingService;
import org.festimate.team.participant.entity.Participant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingServiceImpl implements MatchingService {
    private final MatchingRepository matchingRepository;

    @Override
    @Transactional
    public Matching createMatching(Festival festival, Optional<Participant> targetParticipantOptional, Participant participant) {
        Matching matching;

        if (targetParticipantOptional.isEmpty()) {
            matching = Matching.builder().festival(festival).applicantParticipant(participant).targetParticipant(null).status(MatchingStatus.PENDING).matchDate(LocalDateTime.now()).build();
        } else {
            matching = Matching.builder().festival(festival).applicantParticipant(participant).targetParticipant(targetParticipantOptional.get()).status(MatchingStatus.COMPLETED).matchDate(LocalDateTime.now()).build();
        }

        return matchingRepository.save(matching);
    }

    @Override
    public Optional<Participant> findMatchingCandidate(long festivalId, Participant participant) {
        return matchingRepository.findMatchingCandidate(participant.getParticipantId(), participant.getTypeResult(), participant.getUser().getGender(), festivalId);
    }

    @Transactional
    @Override
    public void matchPendingParticipants(Participant newParticipant) {
        Optional<Matching> pendingMatchingOptional = matchingRepository.findFirstPendingMatchingByTypeAndOppositeGender(
                newParticipant.getFestival().getFestivalId(),
                newParticipant.getTypeResult(),
                newParticipant.getUser().getGender()
        );

        pendingMatchingOptional.ifPresent(pendingMatching -> {
            pendingMatching.completeMatching(newParticipant);
        });
    }


}
