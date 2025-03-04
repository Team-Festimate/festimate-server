package org.festimate.team.user.dto;

public record SignUpResponse(Long userId,
                             String accessToken,
                             String refreshToken
) {
}