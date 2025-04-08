package org.festimate.team.auth.infra.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.auth.infra.KakaoOAuthClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginService {

    private final KakaoOAuthClient kakaoOAuthClient;

    @Transactional
    public String getKakaoPlatformId(String accessToken) {
        // String kakaoAccessToken = kakaoOAuthClient.getAccessToken(accessToken);
        log.info("kakaoAccessToken: {}", accessToken);

        return kakaoOAuthClient.getPlatformId(accessToken);
    }
}