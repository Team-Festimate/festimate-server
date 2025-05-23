package org.festimate.team.api.participant.dto;

import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.TypeResult;
import org.festimate.team.domain.user.entity.AppearanceType;
import org.festimate.team.domain.user.entity.Gender;
import org.festimate.team.domain.user.entity.Mbti;
import org.festimate.team.domain.user.entity.User;

public record DetailProfileResponse(
        String nickname,
        Gender gender,
        int birthYear,
        Mbti mbti,
        AppearanceType appearanceType,
        String introduction,
        String message,
        TypeResult typeResult
) {
    public static DetailProfileResponse from(User user, Participant participant) {
        return new DetailProfileResponse(
                user.getNickname(),
                user.getGender(),
                user.getBirthYear(),
                user.getMbti(),
                user.getAppearanceType(),
                participant.getIntroduction(),
                participant.getMessage(),
                participant.getTypeResult()
        );
    }
}
