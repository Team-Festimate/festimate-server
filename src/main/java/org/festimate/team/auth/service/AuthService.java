package org.festimate.team.auth.service;

import org.festimate.team.user.dto.TokenResponse;

public interface AuthService {
    TokenResponse generateTokens(Long userId);

}
