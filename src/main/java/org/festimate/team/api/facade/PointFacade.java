package org.festimate.team.api.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.api.point.dto.PointHistoryResponse;
import org.festimate.team.api.point.dto.RechargePointRequest;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.festival.service.FestivalService;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.service.ParticipantService;
import org.festimate.team.domain.point.service.PointService;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.service.UserService;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.global.response.ResponseError;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PointFacade {

    private final UserService userService;
    private final FestivalService festivalService;
    private final ParticipantService participantService;
    private final PointService pointService;

    @Transactional(readOnly = true)
    public PointHistoryResponse getMyPointHistory(Long userId, Long festivalId) {
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        Participant participant = getExistingParticipantOrThrow(userId, festival);
        return pointService.getPointHistory(participant);
    }

    @Transactional(readOnly = true)
    public PointHistoryResponse getParticipantPointHistory(Long userId, Long festivalId, Long participantId) {
        User user = userService.getUserByIdOrThrow(userId);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        isHost(user, festival);
        Participant participant = participantService.getParticipantById(participantId);
        return pointService.getPointHistory(participant);
    }

    @Transactional
    public void rechargePoints(Long userId, Long festivalId, RechargePointRequest request) {
        User user = userService.getUserByIdOrThrow(userId);
        Festival festival = festivalService.getFestivalByIdOrThrow(festivalId);
        isHost(user, festival);

        Participant participant = participantService.getParticipantById(request.participantId());
        if (participant.getFestival() != festival) {
            throw new FestimateException(ResponseError.FORBIDDEN_RESOURCE);
        }
        pointService.rechargePoint(participant, request.point());
    }

    private Participant getExistingParticipantOrThrow(Long userId, Festival festival) {
        User user = userService.getUserByIdOrThrow(userId);
        Participant participant = participantService.getParticipant(user, festival);
        if (participant == null) {
            throw new FestimateException(ResponseError.FORBIDDEN_RESOURCE);
        }
        return participant;
    }

    private void isHost(User user, Festival festival) {
        if (!festivalService.isHost(user, festival)) {
            throw new FestimateException(ResponseError.FORBIDDEN_RESOURCE);
        }
    }
}