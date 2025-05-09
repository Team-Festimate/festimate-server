package org.festimate.team.domain.user.dto;

import org.festimate.team.domain.user.entity.AppearanceType;

public record UserInfoDto(
        String nickname,
        AppearanceType appearanceType
) {

}
