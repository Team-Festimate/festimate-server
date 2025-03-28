package org.festimate.team.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.auth.infra.service.KakaoLoginService;
import org.festimate.team.global.jwt.JwtService;
import org.festimate.team.global.jwt.TokenResponse;
import org.festimate.team.user.entity.Platform;
import org.festimate.team.user.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFacade {

    private final JwtService jwtService;
    private final UserService userService;
    private final KakaoLoginService kakaoLoginService;

    @Transactional
    public TokenResponse login(String platformId, Platform platform) {
        return userService.getUserIdByPlatformAndPlatformId(platform, platformId)
                .map(this::login)
                .orElseGet(() -> createTemporaryToken(platformId));
    }

    private TokenResponse login(Long userId) {
        log.info("기존 유저 로그인 성공 - userId: {}", userId);
        String newRefreshToken = jwtService.createRefreshToken(userId);

        userService.updateRefreshToken(userId, newRefreshToken);
        return new TokenResponse(userId, jwtService.createAccessToken(userId), newRefreshToken);
    }

    public String getPlatformId(String authorization) {
        return kakaoLoginService.getKakaoPlatformId(authorization);
    }

    private TokenResponse createTemporaryToken(String platformId) {
        return new TokenResponse(null, jwtService.createTempAccessToken(platformId), jwtService.createTempRefreshToken(platformId));
    }
}


