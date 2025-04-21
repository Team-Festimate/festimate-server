package org.festimate.team.festival.validator;

import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ResponseError;
import org.festimate.team.exception.FestimateException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class DateValidator {
    public static void isFestivalDateValid(final LocalDate startDateStr, final LocalDate endDateStr) {
        log.info("Validating festival dates. startDate={}, endDate={}", startDateStr, endDateStr);

        LocalDate startDate = parseAndValidateDate(startDateStr);
        LocalDate endDate = parseAndValidateDate(endDateStr);

        if (startDate.isAfter(endDate)) {
            log.warn("Invalid festival dates: startDate is after endDate");
            throw new FestimateException(ResponseError.INVALID_DATE_TYPE);
        }

        log.info("Festival dates are valid.");
    }

    public static void isMatchingStartTimeValid(final LocalDate startDateStr, final LocalDateTime matchingStartAt) {
        log.info("Validating matchingStartAt. startDate={}, matchingStartAt={}", startDateStr, matchingStartAt);

        LocalDateTime startTime = parseAndValidateTime(matchingStartAt);

        if (startTime.toLocalDate().isBefore(startDateStr)) {
            log.warn("Invalid matchingStartAt: matchingStartAt is before festival startDate");
            throw new FestimateException(ResponseError.INVALID_TIME_TYPE);
        }

        log.info("matchingStartAt is valid.");
    }

    private static LocalDate parseAndValidateDate(final LocalDate date) {
        if (date == null) {
            throw new FestimateException(ResponseError.INVALID_DATE_TYPE);
        }

        return date;
    }

    private static LocalDateTime parseAndValidateTime(final LocalDateTime time) {
        if (time == null) {
            throw new FestimateException(ResponseError.INVALID_TIME_TYPE);
        }

        return time;
    }
}

