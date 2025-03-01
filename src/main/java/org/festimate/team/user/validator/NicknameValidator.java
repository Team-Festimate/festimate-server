package org.festimate.team.user.validator;

import org.festimate.team.common.response.ResponseError;
import org.festimate.team.common.validator.LengthValidator;
import org.festimate.team.exception.FestimateException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class NicknameValidator {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 6;
    private static final Pattern koreanPattern = Pattern.compile("^[가-힣]+$");

    public void validate(String nickname) {
        if (!LengthValidator.rangeLengthCheck(nickname, MIN_LENGTH, MAX_LENGTH))
            throw new FestimateException(ResponseError.INVALID_INPUT_LENGTH);
        if (!koreanPattern.matcher(nickname).find())
            throw new FestimateException(ResponseError.INVALID_INPUT_NICKNAME);
    }
}
