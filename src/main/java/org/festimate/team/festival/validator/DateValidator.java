package org.festimate.team.festival.validator;

import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ResponseError;
import org.festimate.team.exception.FestimateException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class DateValidator {

    public static LocalDate parseAndValidateDate(final LocalDate date) {
        if (date == null) {
            throw new FestimateException(ResponseError.INVALID_DATE_TYPE);
        }

        return date;
    }

    public static void isFestivalDateValid(final LocalDate startDateStr, final LocalDate endDateStr) {
        log.info("입력된 startDate: {}, endDate: {}", startDateStr, endDateStr);

        LocalDate startDate = parseAndValidateDate(startDateStr);
        LocalDate endDate = parseAndValidateDate(endDateStr);

        if (startDate.isAfter(endDate)) {
            throw new FestimateException(ResponseError.INVALID_DATE_TYPE);
        }

        log.info("날짜 검증 완료: startDate={}, endDate={}", startDate, endDate);
    }
}

