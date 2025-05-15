package org.festimate.team.domain.user.validator;

import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.global.response.ResponseError;
import org.festimate.team.global.validator.LengthValidator;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class NicknameValidator {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 6;
    private static final Pattern nicknamePattern = Pattern.compile("^[가-힣a-zA-Z]+$");

    public void validate(String nickname) {
        if (!LengthValidator.rangeLengthCheck(nickname, MIN_LENGTH, MAX_LENGTH))
            throw new FestimateException(ResponseError.INVALID_INPUT_LENGTH);
        if (!nicknamePattern.matcher(nickname).find())
            throw new FestimateException(ResponseError.INVALID_INPUT_NICKNAME);
    }
}
