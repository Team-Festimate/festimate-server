package org.festimate.team.api.admin.dto;

public record AdminMatchingResponse(
        int successMatchings,
        int totalMatchings
) {
    public static AdminMatchingResponse from(int successMatchings, int totalMatchings) {
        return new AdminMatchingResponse(successMatchings, totalMatchings);
    }
}
