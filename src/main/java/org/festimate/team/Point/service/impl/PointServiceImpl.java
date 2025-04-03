package org.festimate.team.Point.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.Point.dto.PointHistory;
import org.festimate.team.Point.dto.PointHistoryResponse;
import org.festimate.team.Point.entity.Point;
import org.festimate.team.Point.entity.TransactionType;
import org.festimate.team.Point.repository.PointRepository;
import org.festimate.team.Point.service.PointService;
import org.festimate.team.common.response.ResponseError;
import org.festimate.team.exception.FestimateException;
import org.festimate.team.participant.entity.Participant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

    @Override
    public PointHistoryResponse getPointHistory(Participant participant) {
        List<PointHistory> histories = PointHistory.from(participant.getPoints());
        int totalPoint = getTotalPointByParticipant(participant);
        return PointHistoryResponse.from(totalPoint, histories);
    }

    @Override
    public int getTotalPointByParticipant(Participant participant) {
        return pointRepository.getTotalPointByParticipant(participant.getParticipantId());
    }

    @Transactional
    @Override
    public void usePoint(Participant participant) {
        int totalPoint = getTotalPointByParticipant(participant);
        if (totalPoint < 1) {
            throw new FestimateException(ResponseError.INSUFFICIENT_POINTS);
        }

        Point pointUsage = Point.builder()
                .participant(participant)
                .point(1)
                .transactionType(TransactionType.DEBIT)
                .build();

        pointRepository.save(pointUsage);
    }

}
