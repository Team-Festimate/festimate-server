package org.festimate.team.user.dto;

public record TokenResponse(Long userId,
                            String accessToken,
                            String refreshToken
) {
}
