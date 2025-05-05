package org.festimate.team.api.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.admin.dto.SearchParticipantResponse;
import org.festimate.team.api.participant.dto.*;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.domain.matching.service.MatchingService;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.domain.point.service.PointService;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.service.UserService;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.global.response.ResponseError;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ParticipantFacade {

    private final UserService userService;
    private final FestivalService festivalService;
    private final ParticipantService participantService;
    private final PointService pointService;
    private final MatchingService matchingService;

    @Transactional(readOnly = true)
    public EntryResponse entryFestival(Long userId, Long festivalId) {
        Participant participant = getParticipant(userId, festivalId);
        return EntryResponse.of(participant);
    }

    @Transactional
    public EntryResponse createParticipant(Long userId, Long festivalId, ProfileRequest request) {
        User user = userService.getUserByIdOrThrow(userId);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        if (participantService.getParticipant(user, festival) != null) {
            throw new FestimateException(ResponseError.PARTICIPANT_ALREADY_EXISTS);
        }
        Participant participant = participantService.createParticipant(user, festival, request);
        matchingService.matchPendingParticipants(participant);

        return EntryResponse.of(participant);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getParticipantProfile(Long userId, Long festivalId) {
        Participant participant = getParticipant(userId, festivalId);
        Festival festival = participant.getFestival();
        return ProfileResponse.of(participant.getTypeResult(), participant.getUser().getNickname(), festival.getMatchingStartTimeStatus());
    }

    @Transactional(readOnly = true)
    public MainUserInfoResponse getParticipantSummary(Long userId, Long festivalId) {
        Participant participant = getParticipant(userId, festivalId);
        int point = pointService.getTotalPointByParticipant(participant);
        return MainUserInfoResponse.from(participant, point, participant.getFestival().getMatchingStartTimeStatus());
    }

    @Transactional(readOnly = true)
    public DetailProfileResponse getParticipantType(Long userId, Long festivalId) {
        Participant participant = getParticipant(userId, festivalId);
        return DetailProfileResponse.from(participant.getUser(), participant);
    }

    @Transactional
    public void modifyMessage(Long userId, Long festivalId, MessageRequest request) {
        Participant participant = getParticipant(userId, festivalId);
        participant.modifyIntroductionAndMessage(request.introduction(), request.message());
    }

    @Transactional(readOnly = true)
    public Participant getParticipant(Long userId, Long festivalId) {
        User user = userService.getUserByIdOrThrow(userId);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        Participant participant = participantService.getParticipant(user, festival);
        if (participant == null) {
            throw new FestimateException(ResponseError.PARTICIPANT_NOT_FOUND);
        }
        return participant;
    }

    @Transactional(readOnly = true)
    public List<SearchParticipantResponse> getParticipantByNickname(Long userId, Long festivalId, String nickname) {
        User user = userService.getUserByIdOrThrow(userId);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        isHost(user, festival);
        List<Participant> participants = participantService.getParticipantByNickname(festival, nickname);
        return SearchParticipantResponse.from(participants);
    }

    private void isHost(User user, Festival festival) {
        if (!festivalService.isHost(user, festival)) {
            throw new FestimateException(ResponseError.FORBIDDEN_RESOURCE);
        }
    }
}
