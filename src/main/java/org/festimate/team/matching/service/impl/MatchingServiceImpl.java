package org.festimate.team.matching.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.matching.entity.Matching;
import org.festimate.team.matching.entity.MatchingStatus;
import org.festimate.team.matching.repository.MatchingRepository;
import org.festimate.team.matching.service.MatchingService;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.participant.entity.TypeResult;
import org.festimate.team.user.entity.Gender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    @Transactional
    @Override
    public Optional<Participant> findBestCandidateByPriority(long festivalId, Participant participant) {
        List<TypeResult> priorities = MATCHING_PRIORITIES.get(participant.getTypeResult());
        Gender myGender = participant.getUser().getGender();

        for (TypeResult priorityType : priorities) {
            Optional<Participant> candidate = matchingRepository.findMatchingCandidate(
                    participant.getParticipantId(),
                    priorityType,
                    myGender,
                    festivalId
            );
            if (candidate.isPresent()) {
                return candidate;
            }
        }

        return Optional.empty();
    }

    @Transactional
    @Override
    public void matchPendingParticipants(Participant newParticipant) {
        List<Matching> pendingMatchings = matchingRepository.findAllPendingMatchingsByFestivalAndOppositeGender(
                newParticipant.getFestival().getFestivalId(),
                newParticipant.getUser().getGender()
        );

        for (Matching pendingMatching : pendingMatchings) {
            Participant applicant = pendingMatching.getApplicantParticipant();

            if (isValidMatch(applicant, newParticipant)) {
                pendingMatching.completeMatching(newParticipant);
                matchingRepository.save(pendingMatching);
            }
        }
    }

    private boolean isValidMatch(Participant applicant, Participant candidate) {
        List<TypeResult> priorities = MATCHING_PRIORITIES.get(applicant.getTypeResult());

        if (!priorities.contains(candidate.getTypeResult())) {
            return false;
        }

        boolean alreadyMatched = matchingRepository.existsCompletedMatching(
                applicant.getParticipantId(),
                candidate.getParticipantId()
        );

        return !alreadyMatched;
    }

    private static final Map<TypeResult, List<TypeResult>> MATCHING_PRIORITIES = Map.of(
            TypeResult.INFLUENCER, List.of(TypeResult.PHOTO, TypeResult.INFLUENCER, TypeResult.NEWBIE, TypeResult.PLANNER, TypeResult.HEALING),
            TypeResult.NEWBIE, List.of(TypeResult.PLANNER, TypeResult.NEWBIE, TypeResult.HEALING, TypeResult.INFLUENCER, TypeResult.PHOTO),
            TypeResult.PHOTO, List.of(TypeResult.INFLUENCER, TypeResult.PHOTO, TypeResult.PLANNER, TypeResult.HEALING, TypeResult.NEWBIE),
            TypeResult.PLANNER, List.of(TypeResult.NEWBIE, TypeResult.HEALING, TypeResult.INFLUENCER, TypeResult.PHOTO, TypeResult.PLANNER),
            TypeResult.HEALING, List.of(TypeResult.HEALING, TypeResult.PLANNER, TypeResult.PHOTO, TypeResult.NEWBIE, TypeResult.INFLUENCER)
    );

}
