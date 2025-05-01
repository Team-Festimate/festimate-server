package org.festimate.team.domain.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.global.response.ResponseError;
import org.festimate.team.domain.auth.properties.KakaoProperties;
import org.festimate.team.domain.auth.entity.Auth;
import org.festimate.team.global.exception.FestimateException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.create();

    private final static String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final static String AUTHORIZATION = "Authorization";
    private final static String ACCESS_TOKEN = "access_token";
    private final static String BEARER = "Bearer ";
    private final static String AUTHORIZATION_TOKEN = "authorization_code";
    private final static String ID = "id";
    private final static String CLIENT_ID = "client_id";
    private final static String GRANT_TYPE = "grant_type";
    private final static String CODE = "code";
    private final static String REDIRECT_URI = "redirect_uri";

    public Auth getUserInfo(String accessToken) {
        log.info("카카오 OAuth 검증 요청 시작");

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHORIZATION, BEARER + accessToken);

        JsonNode response = restClient.get()
                .uri(KAKAO_USER_INFO_URL)
                .headers(h -> h.addAll(headers))
                .retrieve()
                .body(JsonNode.class);

        if (response == null || !response.has(ID)) {
            throw new FestimateException(ResponseError.INVALID_GRANT);
        }

        log.info("kakao login response: {}", response);
        return new Auth(response.get(ID).asText());
    }

    public String getAccessToken(String code) {
        log.info("kakao accessToken apply start");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(GRANT_TYPE, AUTHORIZATION_TOKEN);
        params.add(CLIENT_ID, kakaoProperties.getClientId());
        params.add(REDIRECT_URI, kakaoProperties.getRedirectUri());
        params.add(CODE, code);

        try {
            Map response = restClient.post()
                    .uri(KAKAO_TOKEN_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(params)
                    .retrieve()
                    .body(Map.class);

            if (response == null || !response.containsKey(ACCESS_TOKEN)) {
                throw new FestimateException(ResponseError.INVALID_GRANT);
            }

            return response.get(ACCESS_TOKEN).toString();
        } catch (Exception e) {
            throw new FestimateException(ResponseError.INVALID_GRANT);
        }
    }

    public String getPlatformId(String code) {
        return getUserInfo(code).platformId();
    }
}