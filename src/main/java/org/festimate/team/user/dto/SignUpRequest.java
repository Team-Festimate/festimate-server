package org.festimate.team.user.dto;

import org.festimate.team.user.entity.AppearanceType;
import org.festimate.team.user.entity.Gender;
import org.festimate.team.user.entity.Mbti;
import org.festimate.team.user.entity.Platform;

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
