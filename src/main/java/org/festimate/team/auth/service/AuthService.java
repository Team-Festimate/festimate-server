package org.festimate.team.auth.service;

import org.festimate.team.user.dto.SignUpResponse;
import org.festimate.team.user.dto.TokenResponse;

public interface AuthService {
    TokenResponse login(Long userId, String accessToken);

    SignUpResponse signUp(Long userId);

}
