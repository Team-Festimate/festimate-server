package org.festimate.team.user.validator;

import org.festimate.team.common.response.ResponseError;
import org.festimate.team.common.validator.LengthValidator;
import org.festimate.team.exception.FestimateException;
import org.springframework.stereotype.Component;

@Component
public class UserRequestValidator {
    public void statusValidate(String status) {
        if (!status.equals("PROGRESS") && !status.equals("END")) {
            throw new FestimateException(ResponseError.INVALID_REQUEST_PARAMETER);
        }
    }
}
