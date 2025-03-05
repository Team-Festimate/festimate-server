package org.festimate.team.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.festimate.team.auth.jwt.JwtProvider;
import org.festimate.team.auth.service.AuthService;
import org.festimate.team.user.dto.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public TokenResponse generateTokens(Long userId) {
        String newAccessToken = jwtProvider.createAccessToken(userId);
        String newRefreshToken = jwtProvider.createRefreshToken(userId);

        return new TokenResponse(userId, newAccessToken, newRefreshToken);
    }
}
