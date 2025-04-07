package org.festimate.team.matching.dto;

import org.festimate.team.matching.entity.Matching;
import org.festimate.team.matching.entity.MatchingStatus;
import org.festimate.team.participant.entity.Participant;
import org.festimate.team.participant.entity.TypeResult;
import org.festimate.team.user.entity.AppearanceType;
import org.festimate.team.user.entity.Gender;
import org.festimate.team.user.entity.Mbti;

public record MatchingInfo(
        Long matchingId,
        MatchingStatus matchingStatus,
        String nickname,
        Gender gender,
        Integer birthYear,
        Mbti mbti,
        AppearanceType appearance,
        TypeResult typeResult
) {
    public static MatchingInfo fromMatching(Matching matching) {
        if (matching.getStatus() == MatchingStatus.PENDING) {
            return new MatchingInfo(
                    matching.getMatchingId(),
                    matching.getStatus(),
                    null, null, null, null, null, null
            );
        }

        Participant participant = matching.getTargetParticipant();
        return new MatchingInfo(
                matching.getMatchingId(),
                matching.getStatus(),
                participant.getUser().getNickname(),
                participant.getUser().getGender(),
                participant.getUser().getBirthYear(),
                participant.getUser().getMbti(),
                participant.getUser().getAppearanceType(),
                participant.getTypeResult()
        );
    }
}

