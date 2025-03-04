package org.festimate.team.auth.service;

import lombok.RequiredArgsConstructor;
import org.festimate.team.auth.client.KakaoApiClient;
import org.festimate.team.auth.dto.TokenResponse;
import org.festimate.team.auth.security.JwtProvider;
import org.festimate.team.user.entity.Platform;
import org.festimate.team.user.entity.User;
import org.festimate.team.user.respository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final KakaoApiClient kakaoApiClient;

    @Transactional
    public TokenResponse kakaoLogin(String code) {
        String accessToken = kakaoApiClient.getAccessToken(code);
        String platformId = kakaoApiClient.getPlatformId(accessToken);

        User user = userRepository.findByPlatformId(platformId).orElseGet(() -> {
            User newUser = User.builder()
                    .platformId(platformId)
                    .platform(Platform.KAKAO)
                    .build();
            return userRepository.save(newUser);
        });

        String newAccessToken = jwtProvider.createAccessToken(user.getUserId().toString());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getUserId().toString());

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
