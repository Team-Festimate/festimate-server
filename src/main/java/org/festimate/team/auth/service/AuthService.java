package org.festimate.team.auth.service;

import org.festimate.team.user.dto.TokenResponse;

public interface AuthService {
    TokenResponse login(Long userId, String accessToken);

    TokenResponse signUp(Long userId);

}
