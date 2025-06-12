package org.festimate.team.domain.matching.validator;

import lombok.extern.slf4j.Slf4j;
import org.festimate.team.domain.festival.entity.Festival;
import org.festimate.team.domain.matching.entity.Matching;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.global.response.ResponseError;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MatchingValidator {

    public static void isMatchingDateValid(final LocalDateTime requestMatchingTime, final LocalDateTime matchingStartAt) {
        log.info("Validating requestMatchingTime. requestMatchingTime={}, matchingStartAt={}", requestMatchingTime, matchingStartAt);

        if (requestMatchingTime.isBefore(matchingStartAt)) {
            log.warn("Invalid requestMatchingTime: requestMatchingTime is before festival matchingStartAt");
            throw new FestimateException(ResponseError.INVALID_TIME_TYPE);
        }

        log.info("matchingStartAt is valid.");
    }

    public static void isApplicantParticipantValid(final Matching matching, final Participant participant) {
        if (matching.getApplicantParticipant() != participant) {
            throw new FestimateException(ResponseError.FORBIDDEN_RESOURCE);
        }
    }

    public static void isFestivalMatchValid(final Matching matching, final Festival festival) {
        if (!matching.getFestival().getFestivalId().equals(festival.getFestivalId())) {
            throw new FestimateException(ResponseError.FORBIDDEN_RESOURCE);
        }
    }
}