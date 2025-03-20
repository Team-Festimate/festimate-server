package org.festimate.team.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.common.response.ResponseError;
import org.festimate.team.exception.FestimateException;
import org.festimate.team.festival.dto.EntryResponse;
import org.festimate.team.festival.dto.FestivalRequest;
import org.festimate.team.festival.dto.FestivalResponse;
import org.festimate.team.festival.entity.Festival;
import org.festimate.team.festival.service.FestivalService;
import org.festimate.team.participant.dto.ProfileRequest;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.participant.service.ParticipantService;
import org.festimate.team.user.entity.User;
import org.festimate.team.user.service.UserService;
import org.springframework.stereotype.Component;

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

    public EntryResponse entryFestival(Long userId, Festival festival, ProfileRequest request) {
        User user = userService.getUserById(userId);

        // 이미 참가자로 등록된 경우 예외 발생
        if (participantService.isAlreadyParticipant(user, festival)) {
            throw new FestimateException(ResponseError.PARTICIPANT_ALREADY_EXISTS);
        }

        // 참가자 생성
        Participant participant = participantService.createParticipant(user, festival, request);

        return EntryResponse.of(participant);
    }
}
