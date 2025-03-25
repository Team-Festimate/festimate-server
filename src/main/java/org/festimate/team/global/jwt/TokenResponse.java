package org.festimate.team.global.jwt;

public record TokenResponse(Long userId,
                            String accessToken,
                            String refreshToken
) {
    public static TokenResponse of(Long userId, String accessToken, String refreshToken) {
        return new TokenResponse(userId, accessToken, refreshToken);
    }
}
