package org.festimate.team.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ResponseError;
import org.festimate.team.exception.FestimateException;
import org.festimate.team.global.jwt.JwtService;
import org.festimate.team.global.jwt.TokenResponse;
import org.festimate.team.user.dto.SignUpRequest;
import org.festimate.team.user.entity.User;
import org.festimate.team.user.service.UserService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignUpFacade {
    private final JwtService jwtService;
    private final UserService userService;

    public TokenResponse signUp(String platformId, SignUpRequest request) {
        if (userService.getUserIdByPlatformId(platformId) != null) {
            throw new FestimateException(ResponseError.USER_ALREADY_EXISTS);
        }
        userService.duplicateNickname(request.nickName());
        User user = userService.saveUser(request, platformId);

        log.info("SignUp - userEntity: {}", user);

        return madeTokenResponse(user.getUserId());
    }

    private TokenResponse madeTokenResponse(Long userId) {
        log.info("signup success - userId : {}", userId);

        String accessToken = jwtService.createAccessToken(userId);
        String refreshToken = jwtService.createRefreshToken(userId);

        userService.updateRefreshToken(userId, refreshToken);

        return TokenResponse.of(userId, accessToken, refreshToken);
    }
}
