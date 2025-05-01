package org.festimate.team.api.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.api.auth.dto.TokenResponse;
import org.festimate.team.domain.auth.service.KakaoLoginService;
import org.festimate.team.domain.user.entity.Platform;
import org.festimate.team.domain.user.service.UserService;
import org.festimate.team.infra.jwt.JwtService;
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
        return userService.getUserIdByPlatform(platform, platformId)
                .map(this::loginExistingUser)
                .orElseGet(() -> createTemporaryToken(platformId));
    }

    private TokenResponse loginExistingUser(Long userId) {
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


