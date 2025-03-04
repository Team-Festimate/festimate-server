package org.festimate.team.user.service;

import org.festimate.team.user.dto.TokenResponse;

public interface AuthService {
    TokenResponse kakaoLogin(String code);
}
