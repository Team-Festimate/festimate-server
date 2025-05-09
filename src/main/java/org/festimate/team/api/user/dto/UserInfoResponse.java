package org.festimate.team.api.user.dto;

import org.festimate.team.domain.user.entity.AppearanceType;

public record UserInfoResponse(
        String nickname,
        AppearanceType appearanceType
) {
    public static UserInfoResponse from(String nickname, AppearanceType appearanceType) {
        return new UserInfoResponse(nickname, appearanceType);
    }
}
