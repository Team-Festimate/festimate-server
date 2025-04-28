package org.festimate.team.api.participant.dto;

public record MessageRequest(
        String introduction,
        String message
) {
    public static MessageRequest of(String introduction, String message) {
        return new MessageRequest(introduction, message);
    }
}
