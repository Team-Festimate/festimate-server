package org.festimate.team.domain.user.validator;

import org.festimate.team.global.response.ResponseError;
import org.festimate.team.global.exception.FestimateException;
import org.springframework.stereotype.Component;

@Component
public class UserRequestValidator {
    public void statusValidate(String status) {
        if (!status.equals("PROGRESS") && !status.equals("END")) {
            throw new FestimateException(ResponseError.INVALID_REQUEST_PARAMETER);
        }
    }
}
