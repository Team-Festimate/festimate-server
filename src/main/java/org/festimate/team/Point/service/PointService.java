package org.festimate.team.Point.service;

import org.festimate.team.Point.dto.PointHistoryResponse;
import org.festimate.team.participant.entity.Participant;

public interface PointService {
    PointHistoryResponse getPointHistory(Participant participant);

    int getTotalPointByParticipant(Participant participant);
}
