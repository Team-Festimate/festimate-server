package org.festimate.team.Point.service;

import org.festimate.team.Point.dto.PointHistoryResponse;
import org.festimate.team.participant.entity.Participant;
import org.springframework.transaction.annotation.Transactional;

public interface PointService {
    PointHistoryResponse getPointHistory(Participant participant);

    int getTotalPointByParticipant(Participant participant);

    @Transactional
    void usePoint(Participant participant);
}
