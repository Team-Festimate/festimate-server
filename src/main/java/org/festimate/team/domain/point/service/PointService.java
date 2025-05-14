package org.festimate.team.domain.point.service;

import org.festimate.team.api.point.dto.PointHistoryResponse;
import org.festimate.team.domain.participant.entity.Participant;
import org.springframework.transaction.annotation.Transactional;

public interface PointService {
    PointHistoryResponse getPointHistory(Participant participant);

    int getTotalPointByParticipant(Participant participant);

    @Transactional
    void usePoint(Participant participant);

    @Transactional
    void rechargePoint(Participant participant, int amount);

    @Transactional
    void dischargePoint(Participant participant, int amount);
}
