package org.festimate.team.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.common.response.ResponseError;
import org.festimate.team.exception.FestimateException;
import org.festimate.team.festival.dto.*;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.service.FestivalService;
import org.festimate.team.participant.dto.ProfileRequest;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.participant.service.ParticipantService;
import org.festimate.team.user.dto.UserFestivalResponse;
import org.festimate.team.user.entity.User;
import org.festimate.team.user.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.festimate.team.festival.validator.DateValidator.isFestivalDateValid;
import static org.festimate.team.festival.validator.FestivalRequestValidator.isFestivalValid;

@Component
@RequiredArgsConstructor
public class FestivalFacade {
    private final UserService userService;
    private final FestivalService festivalService;
    private final ParticipantService participantService;

    public FestivalResponse createFestival(Long userId, FestivalRequest request) {
        User host = userService.getUserById(userId);

        isFestivalValid(request.title(), request.category());
        isFestivalDateValid(request.startDate(), request.endDate());

        Festival festival = festivalService.createFestival(host, request);

        return FestivalResponse.from(festival.getFestivalId(), festival.getInviteCode());
    }

    @Transactional(readOnly = true)
    public EntryResponse enterFestival(Long userId, Festival festival) {
        User user = userService.getUserById(userId);
        Participant participant = getExistingParticipantOrThrow(user, festival);

        return EntryResponse.of(participant);
    }

    @Transactional
    public EntryResponse createParticipant(Long userId, Festival festival, ProfileRequest request) {
        User user = userService.getUserById(userId);

        return EntryResponse.of(createParticipantIfValid(user, festival, request));
    }

    @Transactional(readOnly = true)
    public List<UserFestivalResponse> getUserFestivals(Long userId, String status) {
        User user = userService.getUserById(userId);
        return participantService.getFestivalsByUser(user, status).stream()
                .map(UserFestivalResponse::from)
                .toList();
    }

    public MainUserInfoResponse getParticipantAndPoint(Long userId, Festival festival) {
        User user = userService.getUserById(userId);
        Participant participant = getExistingParticipantOrThrow(user, festival);

        int point = participantService.getTotalPointByParticipant(participant);

        return MainUserInfoResponse.from(participant, point);
    }

    public ProfileResponse getParticipantProfile(Long userId, Festival festival) {
        User user = userService.getUserById(userId);
        Participant participant = getExistingParticipantOrThrow(user, festival);
        return ProfileResponse.of(participant.getTypeResult(), user.getNickname());
    }

    public DetailProfileResponse getParticipantType(Long userId, Festival festival) {
        User user = userService.getUserById(userId);
        Participant participant = getExistingParticipantOrThrow(user, festival);
        return DetailProfileResponse.from(user, participant);
    }

    public void validateUserParticipation(Long userId, Festival festival) {
        User user = userService.getUserById(userId);
        getExistingParticipantOrThrow(user, festival);
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

    private Participant getExistingParticipantOrThrow(User user, Festival festival) {
        Participant participant = participantService.getParticipant(user, festival);
        if (participant == null) {
            throw new FestimateException(ResponseError.FORBIDDEN_RESOURCE);
        }
        return participant;
    }

    private Participant getParticipantInfo(User user, Festival festival) {
        return participantService.getParticipant(user, festival);
    }
}
