package org.festimate.team.festival.dto;

import org.festimate.team.participant.entity.Participant;

public record EntryResponse(
        Long festivalId,
        Long participantId
) {
    public static EntryResponse of(Participant participant) {
        return new EntryResponse(
                participant.getFestival().getFestivalId(),
                participant.getParticipantId()
        );
    }
}
