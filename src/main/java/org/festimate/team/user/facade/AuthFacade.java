package org.festimate.team.user.facade;

import lombok.RequiredArgsConstructor;
import org.festimate.team.user.dto.SignUpRequest;
import org.festimate.team.user.dto.SignUpResponse;
import org.festimate.team.user.dto.TokenResponse;
import org.festimate.team.user.service.AuthService;
import org.festimate.team.user.service.UserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthService authService;
    private final UserService userService;

    public TokenResponse kakaoLogin(String code) {
        return authService.kakaoLogin(code);
    }

    public SignUpResponse signUp(String token, SignUpRequest request) {
        return userService.signUp(token, request);
    }
}
