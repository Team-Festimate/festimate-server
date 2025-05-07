package org.festimate.team.api.matching.dto;

import org.festimate.team.domain.matching.entity.Matching;
import org.festimate.team.domain.participant.entity.Participant;
import org.festimate.team.domain.participant.entity.TypeResult;
import org.festimate.team.domain.user.entity.AppearanceType;
import org.festimate.team.domain.user.entity.Gender;
import org.festimate.team.domain.user.entity.Mbti;

public record MatchingDetailInfo(
        String nickname,
        Gender gender,
        Integer birthYear,
        Mbti mbti,
        AppearanceType appearance,
        String introduction,
        String message,
        TypeResult typeResult
) {
    public static MatchingDetailInfo from(Matching matching) {
        Participant participant = matching.getTargetParticipant();
        return new MatchingDetailInfo(
                participant.getUser().getNickname(),
                participant.getUser().getGender(),
                participant.getUser().getBirthYear(),
                participant.getUser().getMbti(),
                participant.getUser().getAppearanceType(),
                participant.getIntroduction(),
                participant.getMessage(),
                participant.getTypeResult()
        );
    }
}
