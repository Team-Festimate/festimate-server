package org.festimate.team.exception;

import lombok.Getter;
import org.festimate.team.common.response.ResponseError;

@Getter
public class FestimateException extends RuntimeException {
    private final ResponseError responseError;

    public FestimateException(ResponseError responseError) {
        super(responseError.getMessage());
        this.responseError = responseError;
    }
}