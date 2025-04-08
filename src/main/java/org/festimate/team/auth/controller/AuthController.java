package org.festimate.team.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.facade.LoginFacade;
import org.festimate.team.global.jwt.JwtService;
import org.festimate.team.global.jwt.TokenResponse;
import org.festimate.team.user.entity.Platform;
import org.festimate.team.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final LoginFacade loginFacade;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @RequestHeader("Authorization") String kakaoAccessToken
    ) {
        log.info("social login - Code: {}", kakaoAccessToken);

        String platformId = loginFacade.getPlatformId(kakaoAccessToken);

        return userService.getUserIdByPlatformAndPlatformId(Platform.KAKAO, platformId)
                .map(userId -> loginFacade.login(platformId, Platform.KAKAO))
                .map(ResponseBuilder::ok)
                .orElseGet(() -> ResponseBuilder.created(loginFacade.login(platformId, Platform.KAKAO)));
    }

    @PatchMapping("/reissue/token")
    public ResponseEntity<ApiResponse<TokenResponse>> reIssueToken(
            @RequestHeader("Authorization") String refreshToken) {
        return ResponseBuilder.ok(jwtService.reIssueToken(refreshToken));
    }
}
