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

    @Transactional
    public TokenResponse login(Long userId, String accessToken) {
        String newAccessToken = jwtProvider.createAccessToken(userId.toString());
        String newRefreshToken = jwtProvider.createRefreshToken(userId.toString());

        return new TokenResponse(userId, newAccessToken, newRefreshToken);
    }

    @Override
    @Transactional
    public TokenResponse signUp(Long userId) {

        String accessToken = jwtProvider.createAccessToken(userId.toString());
        String refreshToken = jwtProvider.createRefreshToken(userId.toString());

        return new TokenResponse(userId, accessToken, refreshToken);
    }
}
