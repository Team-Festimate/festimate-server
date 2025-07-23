package org.festimate.team.api.auth.dto;

public record TokenResponse(
        Long userId,
        String accessToken,
        String refreshToken
) {
    public static TokenResponse of(Long userId, String accessToken, String refreshToken) {
        return new TokenResponse(userId, accessToken, refreshToken);
    }
}
