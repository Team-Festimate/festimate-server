package org.festimate.team.festival.dto;

public record FestivalResponse(
        Long festivalId,
        String inviteCode
) {
    public static FestivalResponse from(Long festivalId, String inviteCode) {
        return new FestivalResponse(festivalId, inviteCode);
    }
}
