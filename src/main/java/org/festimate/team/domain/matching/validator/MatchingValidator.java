package org.festimate.team.domain.matching.validator;

import lombok.extern.slf4j.Slf4j;
import org.festimate.team.global.response.ResponseError;
import org.festimate.team.global.exception.FestimateException;
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
}