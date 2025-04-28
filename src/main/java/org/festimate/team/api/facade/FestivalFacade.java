package org.festimate.team.api.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.festival.dto.*;
import org.festimate.team.api.participant.dto.ProfileRequest;
import org.festimate.team.api.user.dto.UserFestivalResponse;
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

import static org.festimate.team.domain.festival.validator.DateValidator.isFestivalDateValid;
import static org.festimate.team.domain.festival.validator.DateValidator.isMatchingStartTimeValid;
import static org.festimate.team.domain.festival.validator.FestivalRequestValidator.isFestivalValid;

@Component
@RequiredArgsConstructor
public class FestivalFacade {
    private final UserService userService;
    private final FestivalService festivalService;
    private final ParticipantService participantService;
    private final PointService pointService;
    private final MatchingService matchingService;

    public FestivalResponse createFestival(Long userId, FestivalRequest request) {
        User host = userService.getUserById(userId);

        isFestivalValid(request.title(), request.category());
        isFestivalDateValid(request.startDate(), request.endDate());
        isMatchingStartTimeValid(request.startDate(), request.matchingStartAt());

        Festival festival = festivalService.createFestival(host, request);

        return FestivalResponse.from(festival.getFestivalId(), festival.getInviteCode());
    }

    @Transactional(readOnly = true)
    public EntryResponse enterFestival(Long userId, Festival festival) {
        Participant participant = getExistingParticipantOrThrow(userId, festival);

        return EntryResponse.of(participant);
    }

    @Transactional
    public EntryResponse createParticipant(Long userId, Festival festival, ProfileRequest request) {
        User user = userService.getUserById(userId);

        Participant participant = createParticipantIfValid(user, festival, request);

        matchingService.matchPendingParticipants(participant);
        return EntryResponse.of(participant);
    }

    @Transactional(readOnly = true)
    public List<UserFestivalResponse> getUserFestivals(Long userId, String status) {
        User user = userService.getUserById(userId);
        return participantService.getFestivalsByUser(user, status).stream()
                .map(UserFestivalResponse::from)
                .toList();
    }

    public MainUserInfoResponse getParticipantAndPoint(Long userId, Festival festival) {
        Participant participant = getExistingParticipantOrThrow(userId, festival);

        int point = pointService.getTotalPointByParticipant(participant);

        return MainUserInfoResponse.from(participant, point, festival.getMatchingStartTimeStatus());
    }

    public ProfileResponse getParticipantProfile(Long userId, Festival festival) {
        Participant participant = getExistingParticipantOrThrow(userId, festival);
        return ProfileResponse.of(participant.getTypeResult(), participant.getUser().getNickname(), festival.getMatchingStartTimeStatus());
    }

    public DetailProfileResponse getParticipantType(Long userId, Festival festival) {
        Participant participant = getExistingParticipantOrThrow(userId, festival);
        return DetailProfileResponse.from(participant.getUser(), participant);
    }

    public void validateUserParticipation(Long userId, Festival festival) {
        getExistingParticipantOrThrow(userId, festival);
    }

    @Transactional
    public void modifyMyMessage(Long userId, Festival festival, MessageRequest messageRequest) {
        Participant participant = getExistingParticipantOrThrow(userId, festival);
        if (messageRequest.introduction() == null) {
            throw new FestimateException(ResponseError.BAD_REQUEST);
        }
        participant.modifyIntroductionAndMessage(messageRequest.introduction(), messageRequest.message());
    }

    @Transactional(readOnly = true)
    public List<SearchParticipantResponse> getParticipantByNickname(Long userId, Long festivalId, String nickname) {
        User user = userService.getUserById(userId);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        isHost(user, festival);
        List<Participant> participants = participantService.getParticipantByNickname(festival, nickname);
        return SearchParticipantResponse.from(participants);
    }

    private Participant createParticipantIfValid(User user, Festival festival, ProfileRequest request) {
        if (getParticipantInfo(user, festival) != null) {
            throw new FestimateException(ResponseError.USER_ALREADY_EXISTS);
        }

        if (!festivalService.isFestivalExpired(festival)) {
            throw new FestimateException(ResponseError.EXPIRED_FESTIVAL);
        }
        return participantService.createParticipant(user, festival, request);
    }

    private Participant getExistingParticipantOrThrow(Long userId, Festival festival) {
        User user = userService.getUserById(userId);
        Participant participant = participantService.getParticipant(user, festival);
        if (participant == null) {
            throw new FestimateException(ResponseError.FORBIDDEN_RESOURCE);
        }
        return participant;
    }

    private Participant getParticipantInfo(User user, Festival festival) {
        return participantService.getParticipant(user, festival);
    }

    private void isHost(User user, Festival festival) {
        if (!festivalService.isHost(user, festival)) {
            throw new FestimateException(ResponseError.FORBIDDEN_RESOURCE);
        }
    }
}
