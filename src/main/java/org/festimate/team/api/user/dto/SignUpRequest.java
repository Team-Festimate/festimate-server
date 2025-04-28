package org.festimate.team.api.user.dto;

import org.festimate.team.domain.user.entity.AppearanceType;
import org.festimate.team.domain.user.entity.Gender;
import org.festimate.team.domain.user.entity.Mbti;
import org.festimate.team.domain.user.entity.Platform;

public record SignUpRequest(String name,
                            String phoneNumber,
                            String nickName,
                            int birthYear,
                            Gender gender,
                            Mbti mbti,
                            AppearanceType appearanceType,
                            Platform platform
) {
    public static SignUpRequest toUserSignUp(SignUpRequest request) {
        return new SignUpRequest(
                request.name(),
                request.phoneNumber(),
                request.nickName(),
                request.birthYear(),
                request.gender(),
                request.mbti(),
                request.appearanceType(),
                request.platform()
        );
    }
}
