package org.festimate.team.domain.festival.validator;

import lombok.extern.slf4j.Slf4j;
import org.festimate.team.global.response.ResponseError;
import org.festimate.team.global.validator.LengthValidator;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.domain.festival.entity.Category;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FestivalRequestValidator {
    public static void isFestivalValid(final String title, final String category) {
        isFestivalTitleValid(title);
        isFestivalCategoryValid(category);
    }

    private static void isFestivalTitleValid(final String title) {
        log.info("now value is : {}", title);
        if (title.contains("\n")) {
            log.error("그룹 제목에 엔터가 들어갔습니다.");
            throw new FestimateException(ResponseError.BAD_REQUEST);
        }
        if (!LengthValidator.rangeLengthCheck(title, 1, 20)) {
            log.error("그룹 제목 길이 검증에 실패하였습니다.");
            throw new FestimateException(ResponseError.BAD_REQUEST);
        }
    }

    private static void isFestivalCategoryValid(final String category) {
        log.info("now value is : {}", category);
        try {
            Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FestimateException(ResponseError.BAD_REQUEST);
        }
    }
}
