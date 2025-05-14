package org.festimate.team.domain.point.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.api.point.dto.PointHistory;
import org.festimate.team.api.point.dto.PointHistoryResponse;
import org.festimate.team.domain.point.entity.Point;
import org.festimate.team.domain.point.entity.TransactionType;
import org.festimate.team.domain.point.repository.PointRepository;
import org.festimate.team.domain.point.service.PointService;
import org.festimate.team.global.response.ResponseError;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.domain.participant.entity.Participant;
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

        Point pointUsage = createPointTransaction(participant, 1, TransactionType.DEBIT);
        pointRepository.save(pointUsage);
    }

    @Transactional
    @Override
    public void rechargePoint(Participant participant, int amount) {
        Point pointUsage = createPointTransaction(participant, amount, TransactionType.CREDIT);
        pointRepository.save(pointUsage);
    }

    @Transactional
    @Override
    public void dischargePoint(Participant participant, int amount) {
        Point pointUsage = createPointTransaction(participant, amount, TransactionType.DEBIT);
        pointRepository.save(pointUsage);
    }

    private Point createPointTransaction(Participant participant, int amount, TransactionType type) {
        return Point.builder()
                .participant(participant)
                .point(amount)
                .transactionType(type)
                .build();
    }
}
