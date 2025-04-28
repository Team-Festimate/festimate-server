package org.festimate.team.global.exception;

import lombok.Getter;
import org.festimate.team.global.response.ResponseError;

@Getter
public class FestimateException extends RuntimeException {
    private final ResponseError responseError;

    public FestimateException(ResponseError responseError) {
        super(responseError.getMessage());
        this.responseError = responseError;
    }
}