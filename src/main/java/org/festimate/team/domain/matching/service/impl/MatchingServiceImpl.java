package org.festimate.team.domain.matching.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.api.matching.dto.MatchingInfo;
import org.festimate.team.api.matching.dto.MatchingListResponse;
import org.festimate.team.api.matching.dto.MatchingStatusResponse;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.domain.matching.entity.Matching;
import org.festimate.team.domain.matching.entity.MatchingStatus;
import org.festimate.team.domain.matching.repository.MatchingRepository;
import org.festimate.team.domain.matching.service.MatchingService;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.TypeResult;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.domain.point.service.PointService;
import org.festimate.team.domain.user.entity.Gender;
import org.festimate.team.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.festimate.team.domain.matching.validator.MatchingValidator.isMatchingDateValid;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingServiceImpl implements MatchingService {
    private final MatchingRepository matchingRepository;
    private final FestivalService festivalService;
    private final ParticipantService participantService;
    private final PointService pointService;
    private final UserService userService;

    @Transactional
    public MatchingStatusResponse createMatching(Long userId, Long festivalId) {
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        Participant participant = participantService.getParticipantOrThrow(
                userService.getUserByIdOrThrow(userId), festival
        );

        isMatchingDateValid(LocalDateTime.now(), festival.getMatchingStartAt());
        pointService.usePoint(participant);

        Optional<Participant> targetOptional = findBestCandidateByPriority(festivalId, participant);
        Participant target = targetOptional.orElse(null);

        Matching matching = saveMatching(festival, Optional.ofNullable(target), participant);
        return MatchingStatusResponse.of(matching.getStatus(), matching.getMatchingId());
    }

    @Transactional(readOnly = true)
    @Override
    public MatchingListResponse getMatchingList(Long userId, Long festivalId) {
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        Participant participant = participantService.getParticipantOrThrow(
                userService.getUserByIdOrThrow(userId), festival
        );

        List<MatchingInfo> matchings = getMatchingListByParticipant(participant);
        return MatchingListResponse.from(matchings);
    }

    @Transactional
    protected Matching saveMatching(Festival festival, Optional<Participant> targetParticipantOptional, Participant participant) {
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

    private List<MatchingInfo> getMatchingListByParticipant(Participant participant) {
        List<Matching> matchings = matchingRepository.findAllMatchingsByApplicantParticipant(participant);
        return matchings.stream()
                .map(MatchingInfo::fromMatching)
                .toList();
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
            TypeResult.INFLUENCER, List.of(TypeResult.PHOTO, TypeResult.INFLUENCER, TypeResult.NEWBIE),
            TypeResult.NEWBIE, List.of(TypeResult.PLANNER, TypeResult.NEWBIE, TypeResult.HEALING),
            TypeResult.PHOTO, List.of(TypeResult.INFLUENCER, TypeResult.PHOTO, TypeResult.PLANNER),
            TypeResult.PLANNER, List.of(TypeResult.NEWBIE, TypeResult.HEALING, TypeResult.INFLUENCER),
            TypeResult.HEALING, List.of(TypeResult.HEALING, TypeResult.PLANNER, TypeResult.PHOTO)
    );
}
