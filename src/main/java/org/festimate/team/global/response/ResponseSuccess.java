package org.festimate.team.global.response;

import lombok.Getter;

@Getter
public enum ResponseSuccess {
    OK(2000, "요청이 성공했습니다."),
    CREATED(2010, "요청이 성공했습니다.");

    private final int code;
    private final String message;

    ResponseSuccess(int code, String message) {
        this.code = code;
        this.message = message;
    }

}