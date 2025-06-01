package org.festimate.team.infra.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.api.auth.dto.TokenResponse;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.service.UserService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class JwtService {
    private final JwtTokenProvider tokenProvider;
    private final JwtParser jwtParser;
    private final UserService userService;

    public TokenResponse reIssueToken(String refreshToken) {
        Long userId = jwtParser.getUserIdFromToken(refreshToken);
        User user = userService.getUserByIdOrThrow(userId);
        userService.validateRefreshToken(user, refreshToken);
        String newRefreshToken = tokenProvider.createRefreshToken(userId);
        user.updateRefreshToken(newRefreshToken);

        return TokenResponse.of(userId, tokenProvider.createAccessToken(userId), newRefreshToken);
    }

    public String extractPlatformUserIdFromToken(String token) {
        return jwtParser.getPlatformIdFromToken(token);
    }
}

