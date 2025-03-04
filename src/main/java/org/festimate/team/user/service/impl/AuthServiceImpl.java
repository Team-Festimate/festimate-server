package org.festimate.team.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.festimate.team.user.dto.TokenResponse;
import org.festimate.team.user.entity.User;
import org.festimate.team.user.infra.KakaoApiClient;
import org.festimate.team.user.respository.UserRepository;
import org.festimate.team.user.security.JwtProvider;
import org.festimate.team.user.service.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final KakaoApiClient kakaoApiClient;

    @Transactional
    public TokenResponse kakaoLogin(String code) {
        String accessToken = kakaoApiClient.getAccessToken(code);
        String platformId = kakaoApiClient.getPlatformId(accessToken);
        User user = userRepository.findByPlatformId(platformId).orElse(null);

        if (user == null) {
            return new TokenResponse(null, accessToken, null);
        }

        String newAccessToken = jwtProvider.createAccessToken(user.getUserId().toString());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getUserId().toString());

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new TokenResponse(user.getUserId(), newAccessToken, newRefreshToken);
    }
}
