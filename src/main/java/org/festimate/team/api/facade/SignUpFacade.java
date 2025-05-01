package org.festimate.team.api.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.festimate.team.api.auth.dto.TokenResponse;
import org.festimate.team.api.user.dto.SignUpRequest;
import org.festimate.team.domain.user.entity.User;
import org.festimate.team.domain.user.service.UserService;
import org.festimate.team.domain.user.validator.NicknameValidator;
import org.festimate.team.global.exception.FestimateException;
import org.festimate.team.global.response.ResponseError;
import org.festimate.team.infra.jwt.JwtService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignUpFacade {
    private final JwtService jwtService;
    private final UserService userService;
    private final NicknameValidator nicknameValidator;

    public TokenResponse signUp(String platformId, SignUpRequest request) {
        if (userService.getUserIdByPlatformId(platformId) != null) {
            throw new FestimateException(ResponseError.USER_ALREADY_EXISTS);
        }
        User user = userService.signUp(request, platformId);
        log.info("SignUp - userEntity: {}", user);
        return createTokenResponse(user);
    }

    public void validateNickname(String nickname) {
        nicknameValidator.validate(nickname);
        userService.validateDuplicateNickname(nickname);
    }

    private TokenResponse createTokenResponse(User user) {
        log.info("signup success - userId : {}, nickname : {}", user.getUserId(), user.getNickname());
        String accessToken = jwtService.createAccessToken(user.getUserId());
        String refreshToken = jwtService.createRefreshToken(user.getUserId());
        userService.updateRefreshToken(user.getUserId(), refreshToken);
        return TokenResponse.of(user.getUserId(), accessToken, refreshToken);
    }
}
