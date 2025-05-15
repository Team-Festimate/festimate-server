package org.festimate.team.api.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.admin.dto.AdminMatchingResponse;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.domain.matching.service.MatchingService;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.service.UserService;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.global.response.ResponseError;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MatchingFacade {

    private final UserService userService;
    private final FestivalService festivalService;
    private final ParticipantService participantService;
    private final MatchingService matchingService;

    @Transactional(readOnly = true)
    public AdminMatchingResponse getMatchingSize(Long userId, Long festivalId, Long participantId) {
        User user = userService.getUserByIdOrThrow(userId);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        isHost(user, festival);
        Participant participant = participantService.getParticipantById(participantId);

        return matchingService.getMatchingSize(participant);
    }

    private void isHost(User user, Festival festival) {
        if (!festivalService.isHost(user, festival)) {
            throw new FestimateException(ResponseError.FORBIDDEN_RESOURCE);
        }
    }
}