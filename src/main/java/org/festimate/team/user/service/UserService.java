package org.festimate.team.user.service;

import org.festimate.team.user.dto.SignUpRequest;
import org.festimate.team.user.dto.SignUpResponse;

public interface UserService {
    void duplicateNickname(String nickname);

    SignUpResponse signUp(String token, SignUpRequest signUpRequest);

    Long getUserIdByPlatformId(String platformId);

    void updateRefreshToken(Long userId, String refreshToken);
}
