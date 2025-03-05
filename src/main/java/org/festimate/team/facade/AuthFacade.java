package org.festimate.team.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.auth.infra.KakaoApiClient;
import org.festimate.team.auth.service.AuthService;
import org.festimate.team.user.dto.SignUpRequest;
import org.festimate.team.user.dto.TokenResponse;
import org.festimate.team.user.service.UserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;
    private final UserService userService;
    private final KakaoApiClient kakaoApiClient;

    public TokenResponse login(String code) {
        String accessToken = kakaoApiClient.getAccessToken(code);
        String platformId = kakaoApiClient.getPlatformId(accessToken);

        Long userId = userService.getUserIdByPlatformId(platformId);

        if (userId == null) {
            return new TokenResponse(null, accessToken, null);
        }

        return generateRefreshToken(userId);
    }

    public TokenResponse signUp(String token, SignUpRequest request) {
        String platformId = kakaoApiClient.getPlatformId(token);

        userService.duplicateNickname(request.nickName());
        Long userId = userService.saveUser(request, platformId);

        return generateRefreshToken(userId);
    }

    private TokenResponse generateRefreshToken(Long userId) {
        TokenResponse response = authService.generateTokens(userId);
        userService.updateRefreshToken(response.userId(), response.refreshToken());

        return response;
    }
}
