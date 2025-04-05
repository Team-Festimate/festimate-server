package org.festimate.team.matching.dto;

import org.festimate.team.matching.entity.Matching;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.participant.entity.TypeResult;
import org.festimate.team.user.entity.AppearanceType;
import org.festimate.team.user.entity.Gender;
import org.festimate.team.user.entity.Mbti;

public record MatchingDetailResponse(
        String nickname,
        Gender gender,
        Integer birthYear,
        Mbti mbti,
        AppearanceType appearance,
        TypeResult typeResult,
        String introduction,
        String message
) {
    public static MatchingDetailResponse from(Matching matching) {
        Participant participant = matching.getTargetParticipant();
        return new MatchingDetailResponse(
                participant.getUser().getNickname(),
                participant.getUser().getGender(),
                participant.getUser().getBirthYear(),
                participant.getUser().getMbti(),
                participant.getUser().getAppearanceType(),
                participant.getTypeResult(),
                participant.getIntroduction(),
                participant.getMessage()
        );
    }
}
