package org.festimate.team.api.admin.dto;

import org.festimate.team.domain.participant.entity.Participant;

import java.util.List;

public record SearchParticipantResponse(
        long participantId,
        String name,
        String nickname,
        String phone
) {
    public static SearchParticipantResponse from(Participant participant) {
        return new SearchParticipantResponse(
                participant.getParticipantId(),
                participant.getUser().getName(),
                participant.getUser().getNickname(),
                participant.getUser().getPhoneNumber()
        );
    }

    public static List<SearchParticipantResponse> from(List<Participant> participants) {
        return participants.stream()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .map(SearchParticipantResponse::from)
                .toList();
    }
}
